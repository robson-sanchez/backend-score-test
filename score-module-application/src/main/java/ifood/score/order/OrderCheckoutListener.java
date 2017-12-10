package ifood.score.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Handles checkout order event.
 *
 * Created by robson on 03/12/17.
 */
@Component
public class OrderCheckoutListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderCheckoutListener.class);

  private final OrderMessageProducer messageProducer;

  private final OrderRepository repository;

  public OrderCheckoutListener(OrderMessageProducer messageProducer, OrderRepository repository) {
    this.messageProducer = messageProducer;
    this.repository = repository;
  }

  @JmsListener(destination = "checkout-order")
  public void receiveMessage(Order order) {
    LOGGER.info("Order received: " + order);

    repository.addOrder(order);

    // PROCESS ORDER (Out of scope)

    messageProducer.sendEvents(order, OrderStatus.CHECKOUT);
  }

}
