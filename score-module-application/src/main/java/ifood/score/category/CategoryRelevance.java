package ifood.score.category;

import ifood.score.menu.Category;
import ifood.score.order.OrderStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by robson on 06/12/17.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"order", "category"})
public class CategoryRelevance implements Serializable {

  private UUID order;

  private Category category;

  private Double relevance;

  private OrderStatus orderStatus;

}
