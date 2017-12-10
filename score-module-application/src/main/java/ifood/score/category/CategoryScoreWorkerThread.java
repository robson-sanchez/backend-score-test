package ifood.score.category;

import com.hazelcast.core.HazelcastInstance;
import ifood.score.Relevance;
import ifood.score.ScoreWorkerThread;
import ifood.score.menu.Category;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class CategoryScoreWorkerThread extends ScoreWorkerThread<Category> {

  public CategoryScoreWorkerThread(CategoryRelevanceRepository relevanceRepository,
      CategoryScoreRepository scoreRepository, HazelcastInstance hazelcastInstance) {
    super(relevanceRepository, scoreRepository, hazelcastInstance);
  }

  @Scheduled(fixedDelay = 2*1000)
  public void checkCategoryRelevances() {
    processRelevances();
  }

  @Override
  protected String getLockName() {
    return "category-lock";
  }

  @Override
  protected Category getKey(Relevance relevance) {
    return ((CategoryRelevance) relevance).getCategory();
  }

}
