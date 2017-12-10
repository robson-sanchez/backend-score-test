package ifood.score.order;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class OrderExpirationAudit {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderExpirationAudit.class);

  private final OrderMessageProducer messageProducer;

  private final OrderExpirationRepository expirationRepository;

  private final OrderRepository repository;

  private final HazelcastInstance hazelcastInstance;

  public OrderExpirationAudit(OrderMessageProducer messageProducer,
      OrderExpirationRepository expirationRepository, OrderRepository repository,
      HazelcastInstance hazelcastInstance) {
    this.messageProducer = messageProducer;
    this.expirationRepository = expirationRepository;
    this.repository = repository;
    this.hazelcastInstance = hazelcastInstance;
  }

  @Scheduled(fixedDelay=30*1000)
  public void checkExpiredOrders() {
    ILock lock = hazelcastInstance.getLock("expired-orders");

    if (!lock.tryLock()) {
      LOGGER.info("Another process is calculating the expired orders");
      return;
    }

    try {
      long start = expirationRepository.getStartTimestamp();
      long end = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30);

      List<Order> orders = this.repository.getOrdersByPeriod(start, end);

      orders.stream().forEach(order -> messageProducer.sendEvents(order, OrderStatus.EXPIRATION));

      updateStartTimestamp(end, orders);
    } finally {
      lock.unlock();
    }

  }

  private void updateStartTimestamp(long timestamp, List<Order> orders) {
    try {
      expirationRepository.updateStartTimestamp(timestamp);
    } catch (Exception e) {
      // Rollback
      LOGGER.error("Fail to update expiration timestamp, rollback process");

      orders.stream().forEach(order -> messageProducer.sendEvents(order, OrderStatus.CHECKOUT));
    }
  }

}
