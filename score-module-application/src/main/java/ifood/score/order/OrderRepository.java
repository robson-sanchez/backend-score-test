package ifood.score.order;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderRepository {

  private static final OrderRepository INSTANCE = new OrderRepository();

  private Map<UUID, Order> orders = new HashMap<>();

  public static OrderRepository getInstance() {
    return INSTANCE;
  }

  public void addOrder(Order order) {
    this.orders.put(order.getUuid(), order);
  }

  public Order getOrder(UUID uuid) {
    return this.orders.get(uuid);
  }
}
