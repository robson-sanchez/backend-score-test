package ifood.score.menu;

import ifood.score.Score;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

/**
 * Model class to represent the category score.
 *
 * Created by robson on 02/12/17.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"uuid"})
public class MenuScore extends Score implements Serializable {

  private UUID uuid;

}
