package ifood.score.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Model class to represent the category score.
 *
 * Created by robson on 02/12/17.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"uuid"})
public class MenuScore implements Serializable {

  private UUID uuid;

  @JsonIgnore private BigDecimal relevanceSum = BigDecimal.ZERO;

  @JsonIgnore private long totalOrders;

  public void addRelevance(double relevance) {
    this.relevanceSum = this.relevanceSum.add(BigDecimal.valueOf(relevance));
  }

  public double getScore() {
    if (totalOrders == 0) {
      return 0;
    }

    BigDecimal orders = BigDecimal.valueOf(totalOrders);
    return relevanceSum.divide(orders, 10, BigDecimal.ROUND_HALF_EVEN).doubleValue();
  }
}
