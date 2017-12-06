package ifood.score.order;

import static org.junit.Assert.assertEquals;

import ifood.score.menu.Category;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Created by robson on 05/12/17.
 */
public class OrderTest {

  private static final String ITEM1 = "dad0f8ac-9433-40fd-bd43-9ec0c12d5213";

  private static final String ITEM2 = "6208e2fd-45c3-4013-a69a-5f54cb249be0";

  private static final String ITEM3 = "bd2746ce-a975-4bf4-84dc-fedd14273a03";

  @Test
  public void testItemRelevance() {
    Order order = mockOrder();

    Map<UUID, Double> itemsRelevance = order.getItemsRelevance();

    assertEquals(3, itemsRelevance.size());

    Double item1 = itemsRelevance.get(UUID.fromString(ITEM1));
    Double item2 = itemsRelevance.get(UUID.fromString(ITEM2));
    Double item3 = itemsRelevance.get(UUID.fromString(ITEM3));

    assertEquals(new Double(29.942473581853587), item1);
    assertEquals(new Double(30.51285766361453), item2);
    assertEquals(new Double(28.162092397405416), item3);
  }

  @Test
  public void testCategoryRelevance() {
    Order order = mockOrder();

    Map<Category, Double> categoriesRelevance = order.getCategoriesRelevance();

    assertEquals(2, categoriesRelevance.size());

    Double cat1 = categoriesRelevance.get(Category.PIZZA);
    Double cat2 = categoriesRelevance.get(Category.VEGAN);

    assertEquals(new Double(58.131835897380704), cat1);
    assertEquals(new Double(30.51285766361453), cat2);
  }

  private Order mockOrder() {
    Order order = new Order();

    Item item1 = new Item();
    item1.setMenuUuid(UUID.fromString(ITEM1));
    item1.setMenuCategory(Category.PIZZA);
    item1.setMenuUnitPrice(new BigDecimal(26));
    item1.setQuantity(1);

    Item item2 = new Item();
    item2.setMenuUuid(UUID.fromString(ITEM2));
    item2.setMenuCategory(Category.VEGAN);
    item2.setMenuUnitPrice(new BigDecimal(3));
    item2.setQuantity(3);

    Item item3 = new Item();
    item3.setMenuUuid(UUID.fromString(ITEM3));
    item3.setMenuCategory(Category.PIZZA);
    item3.setMenuUnitPrice(new BigDecimal(23));
    item3.setQuantity(1);

    order.setItems(Arrays.asList(item1, item2, item3));

    return order;
  }

}
