package ifood.score;

import ifood.score.model.ProcessingStatus;
import ifood.score.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by robson on 10/12/17.
 */
@Getter
@Setter
public class Relevance implements Serializable {

  private Double relevance;

  private OrderStatus orderStatus;

  private ProcessingStatus processingStatus = ProcessingStatus.UNPROCESSED;

}
