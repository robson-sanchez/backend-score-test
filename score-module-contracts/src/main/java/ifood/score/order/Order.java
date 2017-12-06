package ifood.score.order;

import ifood.score.menu.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = {"uuid"})
public class Order implements Serializable {

  private static final long serialVersionUID = 6532303515962349019L;

  private UUID uuid;
  private UUID customerUuid;
  private UUID restaurantUuid;
  private UUID addressUuid;
  private Date confirmedAt;
  private List<Item> items;

  public Map<UUID, Double> getItemsRelevance() {
    OrderSummary summary = getOrderSummary();

    return this.items.stream().collect(Collectors.toMap(Item::getMenuUuid,
        item -> getItemRelevance(summary.getTotalItems(), summary.getTotalPrice(), item)));
  }

  public Map<Category, Double> getCategoriesRelevance() {
    Map<Category, Double> result = new HashMap<>();

    OrderSummary summary = getOrderSummary();

    Map<Category, List<Item>> mapping = new HashMap<>();

    for (Item item : items) {
      Category menuCategory = item.getMenuCategory();

      if (!mapping.containsKey(menuCategory)) {
        mapping.put(menuCategory, new ArrayList<>());
      }

      mapping.get(menuCategory).add(item);
    }

    for (Map.Entry<Category, List<Item>> entry : mapping.entrySet()) {
      Double categoryRelevance =
          getCategoryRelevance(summary.getTotalItems(), summary.getTotalPrice(), entry.getValue());
      result.put(entry.getKey(), categoryRelevance);
    }

    return result;
  }

  private Double getItemRelevance(int totalItems, BigDecimal totalPrice, Item item) {
    Double itemsQty = item.getQuantity().doubleValue();
    BigDecimal itemsPrice = getPrice(item);

    return getRelevance(totalItems, totalPrice, itemsQty, itemsPrice);
  }

  private Double getCategoryRelevance(int totalItems, BigDecimal totalPrice, List<Item> items) {
    Optional<OrderSummary> summary =
        items.stream().map(item -> new OrderSummary(item.getQuantity(), getPrice(item)))
            .reduce((acc, order) -> {
              int categoryItems = acc.getTotalItems() + order.getTotalItems();
              BigDecimal categoryPrice = acc.getTotalPrice().add(order.getTotalPrice());
              return new OrderSummary(categoryItems, categoryPrice);
            });

    double qty = summary.get().getTotalItems().doubleValue();
    BigDecimal price = summary.get().getTotalPrice();

    return getRelevance(totalItems, totalPrice, qty, price);
  }

  private Double getRelevance(int totalItems, BigDecimal totalPrice, Double itemsQty,
      BigDecimal itemsPrice) {
    double iq = itemsQty / totalItems;
    double ip = 0;

    if (!totalPrice.equals(BigDecimal.ZERO)) {
      ip = itemsPrice.divide(totalPrice, 10, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    return Math.sqrt(iq * ip * 10000);
  }

  private BigDecimal getPrice(Item item) {
    return item.getMenuUnitPrice().multiply(new BigDecimal(item.getQuantity()));
  }

  private OrderSummary getOrderSummary() {
    Integer totalItems = 0;
    BigDecimal totalPrice = BigDecimal.ZERO;

    for (Item item : items) {
      totalItems += item.getQuantity();

      BigDecimal price = getPrice(item);
      totalPrice = totalPrice.add(price);
    }

    return new OrderSummary(totalItems, totalPrice);
  }
}
