package ifood.score.menu;

import ifood.score.model.ProcessingStatus;
import ifood.score.order.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class MenuScoreWorkerThread {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuScoreWorkerThread.class);

  private static final int MAX_SIZE = 100;

  private final MenuRelevanceRepository relevanceRepository;

  private final MenuScoreRepository scoreRepository;

  public MenuScoreWorkerThread(MenuRelevanceRepository relevanceRepository,
      MenuScoreRepository scoreRepository) {
    this.relevanceRepository = relevanceRepository;
    this.scoreRepository = scoreRepository;
  }

  @Scheduled(fixedDelay = 2*1000)
  public void checkMenuRelevances() {
    processRelevances();
  }

  private void processRelevances() {
    try {
      // TODO Get global lock

      Set<MenuRelevance> relevances =
          relevanceRepository.getRelevancesByStatus(ProcessingStatus.UNPROCESSED, MAX_SIZE);

      Set<MenuRelevance> updatedRelevances = relevances.stream()
          .map(menuRelevance -> {
            menuRelevance.setProcessingStatus(ProcessingStatus.PROCESSING);
            return menuRelevance;
          }).collect(Collectors.toSet());

      relevanceRepository.updateRelevances(updatedRelevances);

      // TODO Release global lock

      updatedRelevances.stream()
          .forEach(menuRelevance -> {
            updateScore(menuRelevance);
          });
    } catch (Exception e) {
      LOGGER.error("Fail to start process", e);
    } finally {
      // TODO Release lock if not release yet
    }
  }

  private void updateStatus(MenuRelevance menuRelevance, ProcessingStatus processing) {
    menuRelevance.setProcessingStatus(processing);
    relevanceRepository.updateRelevance(menuRelevance);
  }

  private void updateScore(MenuRelevance menuRelevance) {

    try {
      // TODO Get lock for the item

      Double relevance = menuRelevance.getRelevance();

      if (!menuRelevance.getOrderStatus().equals(OrderStatus.CHECKOUT)) {
        relevance *= -1;
      }

      scoreRepository.updateScore(menuRelevance.getMenu().toString(), relevance);
      afterUpdateScore(menuRelevance, relevance);
    } catch (Exception e) {
      try {
        // Rollback
        updateStatus(menuRelevance, ProcessingStatus.UNPROCESSED);
      } catch (Exception e1) {
        LOGGER.error("Fail rollback process for item: " + menuRelevance.getMenu(), e1);
      }
    } finally {
      // TODO Release lock for item
    }

  }

  private void afterUpdateScore(MenuRelevance menuRelevance, double relevance) {
    try {
      relevanceRepository.removeRelevance(menuRelevance);
    } catch (Exception e) {
      LOGGER.error("Cannot update menu relevance to PROCESSED for item: " + menuRelevance.getMenu(), e);

      double rollback = relevance * -1;

      try {
        // Rollback
        scoreRepository.updateScore(menuRelevance.getMenu().toString(), rollback);
        updateStatus(menuRelevance, ProcessingStatus.UNPROCESSED);
      } catch (Exception e1) {
        LOGGER.error("Fail rollback process for item: " + menuRelevance.getMenu(), e1);
      }
    }
  }

}
