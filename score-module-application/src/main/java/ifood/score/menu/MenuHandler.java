package ifood.score.menu;

import ifood.score.ScoreHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

/**
 * Handles menu item score requests.
 *
 * Created by robson on 02/12/17.
 */
@Component
public class MenuHandler extends ScoreHandler<String, MenuScore> {

  public MenuHandler(MenuScoreRepository scoreRepository) {
    super(scoreRepository);
  }

  @Override
  public Optional<String> getParameter(ServerRequest request) {
    return Optional.ofNullable(request.pathVariable("menu"));
  }

}
