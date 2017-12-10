package ifood.score;

import static ifood.score.ScoreHandler.DEFAULT_COMPARATOR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by robson on 09/12/17.
 */
public abstract class ScoreRepository<K, V extends Score> {

  private Map<K, V> scores = new HashMap<>();

  public V updateScore(K key, double relevance) {
    V score = getScore(key).orElse(buildNewValue(key));
    score.addRelevance(relevance);

    if (relevance < 0) {
      score.incrementTotalOrders(-1);
    } else {
      score.incrementTotalOrders(1);
    }

    this.scores.put(key, score);

    return score;
  }

  public Optional<V> getScore(K key) {
    return Optional.ofNullable(this.scores.get(key));
  }

  public List<V> searchScore(double scoreQuery, String comparator) {
    return this.scores.values()
        .stream()
        .filter(i -> comparator.equals(DEFAULT_COMPARATOR) ? i.getScore() >= scoreQuery
            : i.getScore() < scoreQuery)
        .collect(Collectors.toList());
  }

  protected abstract V buildNewValue(K key);

}
