package ifood.score;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

/**
 * Created by robson on 09/12/17.
 */
public class Score {

  @JsonIgnore private BigDecimal relevanceSum = BigDecimal.ZERO;

  @JsonIgnore private long totalOrders;

  public void addRelevance(double relevance) {
    this.relevanceSum = this.relevanceSum.add(BigDecimal.valueOf(relevance));
  }

  public void incrementTotalOrders(int value) {
    this.totalOrders += value;
  }

  public double getScore() {
    if (totalOrders == 0) {
      return 0;
    }

    BigDecimal orders = BigDecimal.valueOf(totalOrders);
    return relevanceSum.divide(orders, 10, BigDecimal.ROUND_HALF_EVEN).doubleValue();
  }

}
