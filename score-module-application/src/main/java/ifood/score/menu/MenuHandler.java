package ifood.score.menu;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import ifood.score.ScoreHandler;
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
 * Handles menu item score requests.
 *
 * Created by robson on 02/12/17.
 */
@Component
public class MenuHandler implements ScoreHandler {

  private final MenuScoreRepository scoreRepository;

  public MenuHandler(MenuScoreRepository scoreRepository) {
    this.scoreRepository = scoreRepository;
  }

  @Override
  public Mono<ServerResponse> getScore(ServerRequest request) {
    String menu = request.pathVariable("menu");

    Optional<MenuScore> score = scoreRepository.getScore(menu);

    if (score.isPresent()) {
      MenuScore result = score.get();

      return ServerResponse.ok()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(result), MenuScore.class);
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

    List<MenuScore> result = scoreRepository.searchScore(scoreQuery, comparator);

    if (result.isEmpty()) {
      return ServerResponse.noContent().build();
    }

    return ServerResponse.ok().body(Flux.fromIterable(result), MenuScore.class);
  }

}
