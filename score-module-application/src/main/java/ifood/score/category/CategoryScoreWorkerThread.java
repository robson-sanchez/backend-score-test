package ifood.score.category;

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
public class CategoryScoreWorkerThread {

  private static final Logger LOGGER = LoggerFactory.getLogger(CategoryScoreWorkerThread.class);

  private static final int MAX_SIZE = 100;

  private final CategoryRelevanceRepository relevanceRepository;

  private final CategoryScoreRepository scoreRepository;

  public CategoryScoreWorkerThread(CategoryRelevanceRepository relevanceRepository,
      CategoryScoreRepository scoreRepository) {
    this.relevanceRepository = relevanceRepository;
    this.scoreRepository = scoreRepository;
  }

  @Scheduled(fixedDelay = 2*1000)
  public void checkCategoryRelevances() {
    processRelevances();
  }

  private void processRelevances() {
    try {
      // TODO Get global lock

      Set<CategoryRelevance> relevances =
          relevanceRepository.getRelevancesByStatus(ProcessingStatus.UNPROCESSED, MAX_SIZE);

      Set<CategoryRelevance> updatedRelevances = relevances.stream()
          .map(categoryRelevance -> {
            categoryRelevance.setProcessingStatus(ProcessingStatus.PROCESSING);
            return categoryRelevance;
          }).collect(Collectors.toSet());

      relevanceRepository.updateRelevances(updatedRelevances);

      // TODO Release global lock

      updatedRelevances.stream()
          .forEach(categoryRelevance -> {
            updateScore(categoryRelevance);
          });
    } catch (Exception e) {
      LOGGER.error("Fail to start process", e);
    } finally {
      // TODO Release lock if not release yet
    }
  }

  private void updateStatus(CategoryRelevance categoryRelevance, ProcessingStatus processing) {
    categoryRelevance.setProcessingStatus(processing);
    relevanceRepository.updateRelevance(categoryRelevance);
  }

  private void updateScore(CategoryRelevance categoryRelevance) {

    try {
      // TODO Get lock for the item

      Double relevance = categoryRelevance.getRelevance();

      if (!categoryRelevance.getOrderStatus().equals(OrderStatus.CHECKOUT)) {
        relevance *= -1;
      }

      scoreRepository.updateScore(categoryRelevance.getCategory(), relevance);
      afterUpdateScore(categoryRelevance, relevance);
    } catch (Exception e) {
      try {
        // Rollback
        updateStatus(categoryRelevance, ProcessingStatus.UNPROCESSED);
      } catch (Exception e1) {
        LOGGER.error("Fail rollback process for item: " + categoryRelevance.getCategory(), e1);
      }
    } finally {
      // TODO Release lock for item
    }

  }

  private void afterUpdateScore(CategoryRelevance categoryRelevance, double relevance) {
    try {
      relevanceRepository.removeRelevance(categoryRelevance);
    } catch (Exception e) {
      LOGGER.error("Cannot update menu relevance to PROCESSED for item: " + categoryRelevance.getCategory(), e);

      double rollback = relevance * -1;

      try {
        // Rollback
        scoreRepository.updateScore(categoryRelevance.getCategory(), rollback);
        updateStatus(categoryRelevance, ProcessingStatus.UNPROCESSED);
      } catch (Exception e1) {
        LOGGER.error("Fail rollback process for item: " + categoryRelevance.getCategory(), e1);
      }
    }
  }

}
