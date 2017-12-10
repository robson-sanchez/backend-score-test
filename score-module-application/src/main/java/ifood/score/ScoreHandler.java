package ifood.score;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import ifood.score.model.ErrorMessage;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by robson on 02/12/17.
 */
public abstract class ScoreHandler<K, V extends Score> {

  public static final String DEFAULT_COMPARATOR = "above";

  public static final String[] SUPPORTED_COMPARATORS = {DEFAULT_COMPARATOR, "below"};

  private final ScoreRepository<K, V> repository;

  protected ScoreHandler(ScoreRepository repository) {
    this.repository = repository;
  }

  public Mono<ServerResponse> getScore(ServerRequest request) {
    Optional<K> param = getParameter(request);

    if (!param.isPresent()) {
      ErrorMessage errorMessage = new ErrorMessage();
      errorMessage.setMessage("Invalid input data");

      return ServerResponse.badRequest()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(errorMessage), ErrorMessage.class);
    }

    Optional<V> score = repository.getScore(param.get());

    if (score.isPresent()) {
      Score result = score.get();

      return ServerResponse.ok()
          .contentType(APPLICATION_JSON)
          .body(Mono.just(result), Score.class);
    } else {
      return ServerResponse.notFound().build();
    }
  }

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

    List<V> result = repository.searchScore(scoreQuery, comparator);

    if (result.isEmpty()) {
      return ServerResponse.noContent().build();
    }

    Class returnType = result.get(0).getClass();

    return ServerResponse.ok().body(Flux.fromIterable(result), returnType);
  }

  protected abstract Optional<K> getParameter(ServerRequest request);

}
