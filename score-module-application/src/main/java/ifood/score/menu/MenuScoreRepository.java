package ifood.score.menu;

import ifood.score.ScoreRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by robson on 03/12/17.
 */
@Component
public class MenuScoreRepository extends ScoreRepository<String, MenuScore> {

  @Override
  protected MenuScore buildNewValue(String uuid) {
    MenuScore score = new MenuScore();
    score.setUuid(UUID.fromString(uuid));

    return score;
  }

}
