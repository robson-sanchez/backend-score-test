package ifood.score;

import ifood.score.model.ProcessingStatus;
import ifood.score.order.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by robson on 03/12/17.
 */
public abstract class ScoreWorkerThread<K> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScoreWorkerThread.class);

  private static final int MAX_SIZE = 100;

  private final RelevanceRepository relevanceRepository;

  private final ScoreRepository scoreRepository;

  public ScoreWorkerThread(RelevanceRepository relevanceRepository,
      ScoreRepository scoreRepository) {
    this.relevanceRepository = relevanceRepository;
    this.scoreRepository = scoreRepository;
  }

  @Scheduled(fixedDelay = 2*1000)
  public void checkCategoryRelevances() {
    processRelevances();
  }

  protected void processRelevances() {
    try {
      // TODO Get global lock

      Set<Relevance> relevances =
          relevanceRepository.getRelevancesByStatus(ProcessingStatus.UNPROCESSED, MAX_SIZE);

      Set<Relevance> updatedRelevances = relevances.stream()
          .map(relevance -> {
            relevance.setProcessingStatus(ProcessingStatus.PROCESSING);
            return relevance;
          }).collect(Collectors.toSet());

      relevanceRepository.updateRelevances(updatedRelevances);

      // TODO Release global lock

      updatedRelevances.stream().forEach(relevance -> updateScore(relevance));
    } catch (Exception e) {
      LOGGER.error("Fail to start process", e);
    } finally {
      // TODO Release lock if not release yet
    }
  }

  private void updateStatus(Relevance relevance, ProcessingStatus processing) {
    relevance.setProcessingStatus(processing);
    relevanceRepository.updateRelevance(relevance);
  }

  private void updateScore(Relevance relevance) {

    try {
      // TODO Get lock for the item

      Double value = relevance.getRelevance();

      if (!relevance.getOrderStatus().equals(OrderStatus.CHECKOUT)) {
        value *= -1;
      }

      scoreRepository.updateScore(getKey(relevance), value);
      afterUpdateScore(relevance, value);
    } catch (Exception e) {
      try {
        // Rollback
        updateStatus(relevance, ProcessingStatus.UNPROCESSED);
      } catch (Exception e1) {
        LOGGER.error("Fail rollback process for item: " + getKey(relevance), e1);
      }
    } finally {
      // TODO Release lock for item
    }

  }

  private void afterUpdateScore(Relevance relevance, double value) {
    try {
      relevanceRepository.removeRelevance(relevance);
    } catch (Exception e) {
      LOGGER.error("Cannot update menu relevance to PROCESSED for item: " + getKey(relevance), e);

      double rollback = value * -1;

      try {
        // Rollback
        scoreRepository.updateScore(getKey(relevance), rollback);
        updateStatus(relevance, ProcessingStatus.UNPROCESSED);
      } catch (Exception e1) {
        LOGGER.error("Fail rollback process for item: " + getKey(relevance), e1);
      }
    }
  }

  protected abstract K getKey(Relevance relevance);

}
