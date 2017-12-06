package ifood.score.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by robson on 06/12/17.
 */
@Getter
@RequiredArgsConstructor
@ToString
public class OrderSummary {

  private final Integer totalItems;
  private final BigDecimal totalPrice;

}
