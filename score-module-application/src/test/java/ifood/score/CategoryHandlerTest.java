package ifood.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;

import ifood.score.category.CategoryHandler;
import ifood.score.category.CategoryScoreRepository;
import ifood.score.menu.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;

/**
 * Created by robson on 10/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryHandlerTest {

  @Mock
  private CategoryScoreRepository scoreRepository;

  @Mock
  private ServerRequest request;

  @Test
  public void testInvalidParameter() {
    CategoryHandler handler = new CategoryHandler(scoreRepository);

    doReturn("INVALID").when(request).pathVariable("category");

    Optional<Category> result = handler.getParameter(request);
    assertFalse(result.isPresent());
  }

  @Test
  public void testParameter() {
    CategoryHandler handler = new CategoryHandler(scoreRepository);

    doReturn(Category.ITALIAN.name()).when(request).pathVariable("category");

    Optional<Category> result = handler.getParameter(request);
    assertEquals(Category.ITALIAN, result.orElse(Category.OTHER));
  }
}
