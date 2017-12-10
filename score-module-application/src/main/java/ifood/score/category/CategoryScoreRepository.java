package ifood.score.category;

import ifood.score.ScoreRepository;
import ifood.score.menu.Category;
import org.springframework.stereotype.Component;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class CategoryScoreRepository extends ScoreRepository<Category, CategoryScore> {

  @Override
  protected CategoryScore buildNewValue(Category category) {
    CategoryScore score = new CategoryScore();
    score.setCategory(category);

    return score;
  }

}
