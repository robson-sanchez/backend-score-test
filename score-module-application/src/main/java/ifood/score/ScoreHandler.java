package ifood.score;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Created by robson on 02/12/17.
 */
public interface ScoreHandler {

  String DEFAULT_COMPARATOR = "above";

  String[] SUPPORTED_COMPARATORS = {DEFAULT_COMPARATOR, "below"};

  Mono<ServerResponse> getScore(ServerRequest request);

  Mono<ServerResponse> retrieveScoreByParameter(ServerRequest request);

}
