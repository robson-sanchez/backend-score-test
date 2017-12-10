package ifood.score.category;

import ifood.score.Relevance;
import ifood.score.menu.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by robson on 06/12/17.
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"order", "category"})
public class CategoryRelevance extends Relevance {

  private UUID order;

  private Category category;

}
