package ifood.score.category;

import ifood.score.ScoreHandler;
import ifood.score.menu.Category;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

/**
 * Handles category score requests.
 *
 * Created by robson on 02/12/17.
 */
@Component
public class CategoryHandler extends ScoreHandler<Category, CategoryScore> {

  public CategoryHandler(CategoryScoreRepository scoreRepository) {
    super(scoreRepository);
  }

  @Override
  protected Optional<Category> getParameter(ServerRequest request) {
    String categoryParam = request.pathVariable("category");

    try {
      Category category = Category.valueOf(categoryParam);
      return Optional.of(category);
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

}
