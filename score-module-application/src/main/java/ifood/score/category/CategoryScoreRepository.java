package ifood.score.category;

import static ifood.score.ScoreHandler.DEFAULT_COMPARATOR;

import ifood.score.menu.Category;
import ifood.score.menu.MenuScore;
import ifood.score.mock.generator.RandomishPicker;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class CategoryScoreRepository {

  private Map<Category, CategoryScore> scores = new HashMap<>();

  public CategoryScore updateScore(Category category, double relevance) {
    CategoryScore score = getScore(category).orElse(new CategoryScore());
    score.setCategory(category);
    score.addRelevance(relevance);

    if (relevance < 0) {
      score.incrementTotalOrders(-1);
    } else {
      score.incrementTotalOrders(1);
    }

    this.scores.put(category, score);

    return score;
  }

  public Optional<CategoryScore> getScore(Category category) {
    return Optional.ofNullable(this.scores.get(category));
  }

  public List<CategoryScore> searchScore(double scoreQuery, String comparator) {
    return this.scores.values()
        .stream()
        .filter(i -> comparator.equals(DEFAULT_COMPARATOR) ? i.getScore() >= scoreQuery
            : i.getScore() < scoreQuery)
        .collect(Collectors.toList());
  }

}
