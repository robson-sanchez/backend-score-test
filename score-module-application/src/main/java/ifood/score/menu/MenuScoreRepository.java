package ifood.score.menu;

import static ifood.score.ScoreHandler.DEFAULT_COMPARATOR;

import ifood.score.mock.generator.RandomishPicker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class MenuScoreRepository {

  private Map<String, MenuScore> scores = new HashMap<>();

  public MenuScore updateScore(String uuid, double relevance) {
    MenuScore score = getScore(uuid).orElse(new MenuScore());
    score.setUuid(UUID.fromString(uuid));
    score.addRelevance(relevance);

    if (relevance < 0) {
      score.incrementTotalOrders(-1);
    } else {
      score.incrementTotalOrders(1);
    }

    this.scores.put(uuid, score);

    return score;
  }

  public Optional<MenuScore> getScore(String uuid) {
    return Optional.ofNullable(this.scores.get(uuid));
  }

  public List<MenuScore> searchScore(double scoreQuery, String comparator) {
    return this.scores.values()
        .stream()
        .filter(i -> comparator.equals(DEFAULT_COMPARATOR) ? i.getScore() >= scoreQuery
            : i.getScore() < scoreQuery)
        .collect(Collectors.toList());
  }

}
