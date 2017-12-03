package ifood.score.category;

import ifood.score.menu.Category;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Model class to represent the category score.
 *
 * Created by robson on 02/12/17.
 */
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of={"category"})
public class CategoryScore implements Serializable {

  private Category category;
  private double score;

}
