package ifood.score.category;

import ifood.score.menu.MenuRelevance;
import ifood.score.menu.MenuRelevanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Handles category relevance event.
 *
 * Created by robson on 03/12/17.
 */
@Component
public class CategoryRelevanceListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRelevanceListener.class);

  @JmsListener(destination = "menu-relevance")
  public void receiveMessage(List<MenuRelevance> relevances) {
    LOGGER.info("Category relevance received: " + relevances.size());
    MenuRelevanceRepository.getInstance().addRelevances(relevances);
  }

}
