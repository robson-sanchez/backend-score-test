package ifood.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;

import ifood.score.category.CategoryHandler;
import ifood.score.category.CategoryScoreRepository;
import ifood.score.menu.Category;
import ifood.score.menu.MenuHandler;
import ifood.score.menu.MenuScoreRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by robson on 10/12/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MenuHandlerTest {

  @Mock
  private MenuScoreRepository scoreRepository;

  @Mock
  private ServerRequest request;

  @Test
  public void testInvalidParameter() {
    MenuHandler handler = new MenuHandler(scoreRepository);

    Optional<String> result = handler.getParameter(request);
    assertFalse(result.isPresent());
  }

  @Test
  public void testParameter() {
    MenuHandler handler = new MenuHandler(scoreRepository);

    UUID uuid = UUID.randomUUID();

    doReturn(uuid.toString()).when(request).pathVariable("menu");

    Optional<String> result = handler.getParameter(request);
    assertEquals(uuid.toString(), result.orElse(""));
  }
}
