package ifood.score.menu;

import ifood.score.Relevance;
import ifood.score.ScoreWorkerThread;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class MenuScoreWorkerThread extends ScoreWorkerThread<String> {

  public MenuScoreWorkerThread(MenuRelevanceRepository relevanceRepository,
      MenuScoreRepository scoreRepository) {
    super(relevanceRepository, scoreRepository);
  }

  @Scheduled(fixedDelay = 2*1000)
  public void checkMenuRelevances() {
    processRelevances();
  }

  @Override
  protected String getKey(Relevance relevance) {
    return ((MenuRelevance) relevance).getMenu().toString();
  }

}
