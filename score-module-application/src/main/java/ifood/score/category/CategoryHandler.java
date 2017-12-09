package ifood.score.category;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import ifood.score.ScoreHandler;
import ifood.score.menu.Category;
import ifood.score.model.ErrorMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Handles category score requests.
 *
 * Created by robson on 02/12/17.
 */
@Component
public class CategoryHandler implements ScoreHandler {

  private final CategoryScoreRepository scoreRepository;

  public CategoryHandler(CategoryScoreRepository scoreRepository) {
    this.scoreRepository = scoreRepository;
  }

  @Override
  public Mono<ServerResponse> getScore(ServerRequest request) {
    String categoryParam = request.pathVariable("category");

    Category category;

    try {
      category = Category.valueOf(categoryParam);
    } catch (IllegalArgumentException e) {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage("Invalid category");

      return ServerResponse.badRequest()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(errorMessage), ErrorMessage.class);
    }

    Optional<CategoryScore> score = scoreRepository.getScore(category);

    if (score.isPresent()) {
      CategoryScore result = score.get();

      return ServerResponse.ok()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(result), CategoryScore.class);
    } else {
      return ServerResponse.notFound().build();
    }
  }

  @Override
  public Mono<ServerResponse> retrieveScoreByParameter(ServerRequest request) {
    Optional<String> scoreParam = request.queryParam("score");

    if (!scoreParam.isPresent()) {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage("Missing score parameter");

      return ServerResponse.badRequest()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(errorMessage), ErrorMessage.class);
    }

    double scoreQuery;

    try {
      scoreQuery = scoreParam.map(s -> Double.valueOf(s)).get();
    } catch (NumberFormatException e) {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage("Invalid score parameter. It should be numeric value");

      return ServerResponse.badRequest()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(errorMessage), ErrorMessage.class);
    }

    String comparator = request.queryParam("comparator").orElse(DEFAULT_COMPARATOR);

    if (!Arrays.asList(SUPPORTED_COMPARATORS).contains(comparator)) {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage("Invalid comparator parameter");

      return ServerResponse.badRequest()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(errorMessage), ErrorMessage.class);
    }

    List<CategoryScore> result = scoreRepository.searchScore(scoreQuery, comparator);

    if (result.isEmpty()) {
      return ServerResponse.noContent().build();
    }

    return ServerResponse.ok().body(Flux.fromIterable(result), CategoryScore.class);
  }

}
