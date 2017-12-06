package ifood.score.menu;

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
@EqualsAndHashCode(of = {"order", "menu"})
public class MenuRelevance implements Serializable {

  private UUID order;

  private UUID menu;

  private Double relevance;

  private OrderStatus orderStatus;

}
