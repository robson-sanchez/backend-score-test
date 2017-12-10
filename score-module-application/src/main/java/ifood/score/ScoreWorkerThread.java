package ifood.score;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import ifood.score.model.ProcessingStatus;
import ifood.score.order.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by robson on 03/12/17.
 */
public abstract class ScoreWorkerThread<K> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ScoreWorkerThread.class);

  private static final int MAX_SIZE = 100;

  private final RelevanceRepository relevanceRepository;

  private final ScoreRepository scoreRepository;

  private final HazelcastInstance hazelcastInstance;

  public ScoreWorkerThread(RelevanceRepository relevanceRepository,
      ScoreRepository scoreRepository, HazelcastInstance hazelcastInstance) {
    this.relevanceRepository = relevanceRepository;
    this.scoreRepository = scoreRepository;
    this.hazelcastInstance = hazelcastInstance;
  }

  @Scheduled(fixedDelay = 2*1000)
  public void checkCategoryRelevances() {
    processRelevances();
  }

  protected void processRelevances() {
    String lockName = getLockName();

    Optional<ILock> optionalLock = acquireLock(lockName);

    if (!optionalLock.isPresent()) {
      LOGGER.error("Fail to acquire global lock " + lockName);
      return;
    }

    ILock lock = optionalLock.get();

    try {
      Set<Relevance> relevances =
          relevanceRepository.getRelevancesByStatus(ProcessingStatus.UNPROCESSED, MAX_SIZE);

      Set<Relevance> updatedRelevances = relevances.stream()
          .map(relevance -> {
            relevance.setProcessingStatus(ProcessingStatus.PROCESSING);
            return relevance;
          }).collect(Collectors.toSet());

      relevanceRepository.updateRelevances(updatedRelevances);


      lock.unlock();

      updatedRelevances.stream().forEach(relevance -> updateScore(relevance));
    } catch (Exception e) {
      LOGGER.error("Fail to start process", e);
    } finally {
      if (lock.isLocked()) {
        lock.unlock();
      }
    }
  }

  private void updateStatus(Relevance relevance, ProcessingStatus processing) {
    relevance.setProcessingStatus(processing);
    relevanceRepository.updateRelevance(relevance);
  }

  private void updateScore(Relevance relevance) {
    String lockName = getKey(relevance).toString();

    Optional<ILock> optionalLock = acquireLock(lockName);

    if (!optionalLock.isPresent()) {
      LOGGER.error("Fail to acquire lock " + lockName);
      return;
    }

    ILock lock = optionalLock.get();

    try {
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
      if (lock.isLocked()) {
        lock.unlock();
      }
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

  private Optional<ILock> acquireLock(String lockName) {
    ILock lock = hazelcastInstance.getLock(lockName);
    boolean locked;

    // Acquire lock
    try {
      locked = lock.tryLock(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      return Optional.empty();
    }

    return locked ? Optional.of(lock) : Optional.empty();
  }

  protected abstract String getLockName();

  protected abstract K getKey(Relevance relevance);

}
