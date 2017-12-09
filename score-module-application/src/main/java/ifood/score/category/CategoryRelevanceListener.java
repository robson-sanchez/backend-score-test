package ifood.score.category;

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

  private final CategoryRelevanceRepository repository;

  public CategoryRelevanceListener(CategoryRelevanceRepository repository) {
    this.repository = repository;
  }

  @JmsListener(destination = "category-relevance")
  public void receiveMessage(List<CategoryRelevance> relevances) {
    LOGGER.info("Category relevance received: " + relevances.size());
    repository.addRelevances(relevances);
  }

}
