package ifood.score.order;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderRepository {

  private Map<UUID, Order> orders = Collections.synchronizedMap(new HashMap<>());

  public void addOrder(Order order) {
    this.orders.put(order.getUuid(), order);
  }

  public Order getOrder(UUID uuid) {
    return this.orders.get(uuid);
  }

  public List<Order> getOrdersByPeriod(long startTime, long endTime) {
    return this.orders.values()
        .stream()
        .filter(order -> order.getConfirmedAt() != null && order.getConfirmedAt() >= startTime
            && order.getConfirmedAt() <= endTime)
        .collect(Collectors.toList());
  }

}
