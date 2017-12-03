package ifood.score.order;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Handles checkout order event.
 *
 * Created by robson on 03/12/17.
 */
@Component
public class OrderCheckoutListener {

  @JmsListener(destination = "checkout-order")
  public void receiveMessage(Order order) {
    System.out.println("Order received: " + order);
  }

}
