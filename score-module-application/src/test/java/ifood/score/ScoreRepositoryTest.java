package ifood.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import ifood.score.category.CategoryScore;
import ifood.score.category.CategoryScoreRepository;
import ifood.score.menu.Category;
import org.junit.Test;

import java.util.List;

/**
 * Created by robson on 10/12/17.
 */
public class ScoreRepositoryTest {

  @Test
  public void testEmpty() {
    ScoreRepository<Category, CategoryScore> repository = new CategoryScoreRepository();
    assertFalse(repository.getScore(Category.PIZZA).isPresent());
  }

  @Test
  public void testSearch() {
    ScoreRepository<Category, CategoryScore> repository = new CategoryScoreRepository();

    // Mock data
    mockData(repository);

    CategoryScore pizza = repository.getScore(Category.PIZZA).get();
    CategoryScore vegan = repository.getScore(Category.VEGAN).get();
    CategoryScore hamburger = repository.getScore(Category.HAMBURGER).get();

    assertEquals(55.682494179, pizza.getScore(), 0.00000001);
    assertEquals(45.990057944, vegan.getScore(), 0.00000001);
    assertEquals(50.4976222872, hamburger.getScore(), 0.00000001);

    List<CategoryScore> greatherThan = repository.searchScore(50, "above");
    List<CategoryScore> lowerThan = repository.searchScore(50, "below");

    assertEquals(2, greatherThan.size());
    assertEquals(1, lowerThan.size());

    assertEquals(Category.HAMBURGER, greatherThan.get(0).getCategory());
    assertEquals(Category.PIZZA, greatherThan.get(1).getCategory());
    assertEquals(Category.VEGAN, lowerThan.get(0).getCategory());

    // Cancel orders
    repository.updateScore(Category.VEGAN, -66.079797895);
    repository.updateScore(Category.HAMBURGER, -88.209338434);

    pizza = repository.getScore(Category.PIZZA).get();
    vegan = repository.getScore(Category.VEGAN).get();
    hamburger = repository.getScore(Category.HAMBURGER).get();

    assertEquals(55.682494179, pizza.getScore(), 0.00000001);
    assertEquals(35.9451879685, vegan.getScore(), 0.00000001);
    assertEquals(37.9270502383, hamburger.getScore(), 0.00000001);

    greatherThan = repository.searchScore(50, "above");
    lowerThan = repository.searchScore(50, "below");

    assertEquals(1, greatherThan.size());
    assertEquals(2, lowerThan.size());

    assertEquals(Category.PIZZA, greatherThan.get(0).getCategory());
    assertEquals(Category.VEGAN, lowerThan.get(0).getCategory());
    assertEquals(Category.HAMBURGER, lowerThan.get(1).getCategory());
  }

  private void mockData(ScoreRepository<Category, CategoryScore> repository) {
    repository.updateScore(Category.PIZZA, 34.678750393);
    repository.updateScore(Category.VEGAN, 45.349834983);
    repository.updateScore(Category.HAMBURGER, 78.392382232);
    repository.updateScore(Category.PIZZA, 77.409540954);
    repository.updateScore(Category.PIZZA, 55.039847452);
    repository.updateScore(Category.VEGAN, 26.540540954);
    repository.updateScore(Category.VEGAN, 66.079797895);
    repository.updateScore(Category.PIZZA, 59.349854753);
    repository.updateScore(Category.PIZZA, 51.934477343);
    repository.updateScore(Category.HAMBURGER, 88.209338434);
    repository.updateScore(Category.HAMBURGER, 12.358475454);
    repository.updateScore(Category.HAMBURGER, 23.030293029);
  }

}
