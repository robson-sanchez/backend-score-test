package ifood.score.menu;

import ifood.score.category.CategoryRelevance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handles menu item relevance event.
 *
 * Created by robson on 03/12/17.
 */
@Component
public class MenuRelevanceListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuRelevanceListener.class);

  @JmsListener(destination = "item-relevance")
  public void receiveMessage(List<MenuRelevance> relevances) {
    LOGGER.info("Category relevance received: " + relevances.size());
    MenuRelevanceRepository.getInstance().addRelevances(relevances);
  }

}
