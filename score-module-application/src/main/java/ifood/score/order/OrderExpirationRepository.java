package ifood.score.order;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by robson on 10/12/17.
 */
@Component
public class OrderExpirationRepository {

  private long startTimestamp = -1;

  @PostConstruct
  public void init() {
    if (getStartTimestamp() == -1) {
      startTimestamp = System.currentTimeMillis();
    }
  }

  public long getStartTimestamp() {
    return startTimestamp;
  }

  public void updateStartTimestamp(long startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

}
