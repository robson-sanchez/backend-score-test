package ifood.score.order;

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

  @JmsListener(destination = "cancel-order")
  public void receiveMessage(UUID uuid) {
    System.out.println("Order cancelled: " + uuid);
  }

}
