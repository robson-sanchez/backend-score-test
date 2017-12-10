package ifood.score.menu;

import ifood.score.Relevance;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by robson on 06/12/17.
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"order", "menu"})
public class MenuRelevance extends Relevance {

  private UUID order;

  private UUID menu;

}
