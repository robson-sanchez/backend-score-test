package ifood.score.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Handles cancel order event.
 *
 * Created by robson on 03/12/17.
 */
@Component
public class OrderCancelationListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelationListener.class);

  private final OrderMessageProducer messageProducer;

  private final OrderRepository repository;

  public OrderCancelationListener(OrderMessageProducer messageProducer, OrderRepository repository) {
    this.messageProducer = messageProducer;
    this.repository = repository;
  }

  @JmsListener(destination = "cancel-order")
  public void receiveMessage(UUID uuid) {
    LOGGER.info("Order cancellation: " + uuid);
    // PROCESS ORDER (Out of scope)

    Order order = repository.getOrder(uuid);

    if (order != null) {
      messageProducer.sendEvents(order, OrderStatus.CANCELLATION);
    }
  }

}
