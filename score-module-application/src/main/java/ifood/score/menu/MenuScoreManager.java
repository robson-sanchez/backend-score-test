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
public class MenuScoreManager {

  private Map<String, MenuScore> scores = new HashMap<>();

  public MenuScoreManager() {
    buildScores();
  }

  private void buildScores() {
    IntStream.range(0, 50).forEach(i -> {
      MenuScore score = new MenuScore();
      score.setUuid(UUID.randomUUID());

      int totalOrders = RandomishPicker._int(300, 500);
      score.setTotalOrders(totalOrders);

      IntStream.range(0, totalOrders).forEach(order -> {
        Integer relevance = RandomishPicker._int(1, 100);
        score.addRelevance(relevance);
      });

      System.out.println(score);

      this.scores.put(score.getUuid().toString(), score);
    });
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
