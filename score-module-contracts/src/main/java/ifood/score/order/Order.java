package ifood.score.order;

import ifood.score.menu.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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

  public Double getItemRelevance(UUID item) {
    Stream<Item> allItems = items.stream();
    Stream<Item> menuItems = allItems.filter(i -> i.getMenuUuid().equals(item));

    return getRelevance(allItems, menuItems);
  }

  public Double getCategoryRelevance(Category category) {
    Stream<Item> allItems = items.stream();
    Stream<Item> categoryItems = allItems.filter(i -> i.getMenuCategory().equals(category));

    return getRelevance(allItems, categoryItems);
  }

  private Double getRelevance(Stream<Item> allItems, Stream<Item> filteredItems) {
    int totalItems = getQuantity(allItems);
    int itemsQty = getQuantity(filteredItems);

    BigDecimal totalPrice = getPrice(allItems);
    BigDecimal itemPrice = getPrice(filteredItems);

    double iq = itemsQty / totalItems;
    double ip = totalPrice.equals(BigDecimal.ZERO) ? 0 : itemPrice.divide(totalPrice).doubleValue();

    return Math.sqrt(iq * ip * 10000);
  }

  private Integer getQuantity(Stream<Item> items) {
    return items.mapToInt(i -> i.getQuantity()).sum();
  }

  private BigDecimal getPrice(Stream<Item> items) {
    return items.map(i -> i.getMenuUnitPrice().multiply(new BigDecimal(i.getQuantity())))
        .reduce((sum, i) -> sum.add(i)).orElse(BigDecimal.ZERO);
  }
}
