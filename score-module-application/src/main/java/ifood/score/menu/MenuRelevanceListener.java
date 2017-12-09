package ifood.score.menu;

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

  private final MenuRelevanceRepository repository;

  public MenuRelevanceListener(MenuRelevanceRepository repository) {
    this.repository = repository;
  }

  @JmsListener(destination = "menu-relevance")
  public void receiveMessage(List<MenuRelevance> relevances) {
    LOGGER.info("Menu relevance received: " + relevances.size());
    repository.addRelevances(relevances);
  }

}
