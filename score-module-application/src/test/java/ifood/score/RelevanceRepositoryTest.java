package ifood.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ifood.score.category.CategoryRelevance;
import ifood.score.category.CategoryRelevanceRepository;
import ifood.score.menu.Category;
import ifood.score.model.ProcessingStatus;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * Created by robson on 10/12/17.
 */
public class RelevanceRepositoryTest {

  @Test
  public void testUpdateRelevances() {
    RelevanceRepository<CategoryRelevance> repository = new CategoryRelevanceRepository();

    CategoryRelevance relevance1 = new CategoryRelevance();
    relevance1.setCategory(Category.ARABIC);
    relevance1.setOrder(UUID.randomUUID());

    CategoryRelevance relevance2 = new CategoryRelevance();
    relevance2.setCategory(Category.BRAZILIAN);
    relevance2.setOrder(UUID.randomUUID());

    CategoryRelevance relevance3 = new CategoryRelevance();
    relevance3.setCategory(Category.ARABIC);
    relevance3.setOrder(UUID.randomUUID());

    CategoryRelevance relevance4 = new CategoryRelevance();
    relevance4.setCategory(Category.HAMBURGER);
    relevance4.setOrder(UUID.randomUUID());

    repository.addRelevances(Arrays.asList(relevance1, relevance2, relevance3, relevance4));

    Set<CategoryRelevance> relevancesByStatus =
        repository.getRelevancesByStatus(ProcessingStatus.PROCESSING, 10);
    assertTrue(relevancesByStatus.isEmpty());

    relevancesByStatus = repository.getRelevancesByStatus(ProcessingStatus.UNPROCESSED, 10);
    assertEquals(4, relevancesByStatus.size());

    relevance2.setProcessingStatus(ProcessingStatus.PROCESSING);
    relevance3.setProcessingStatus(ProcessingStatus.PROCESSING);
    relevance4.setProcessingStatus(ProcessingStatus.PROCESSING);

    relevancesByStatus = repository.getRelevancesByStatus(ProcessingStatus.UNPROCESSED, 10);
    assertEquals(1, relevancesByStatus.size());

    relevancesByStatus = repository.getRelevancesByStatus(ProcessingStatus.PROCESSING, 10);
    assertEquals(3, relevancesByStatus.size());

    relevance4.setProcessingStatus(ProcessingStatus.UNPROCESSED);

    repository.updateRelevance(relevance4);
    repository.removeRelevance(relevance2);

    relevancesByStatus = repository.getRelevancesByStatus(ProcessingStatus.UNPROCESSED, 10);
    assertEquals(2, relevancesByStatus.size());

    relevancesByStatus = repository.getRelevancesByStatus(ProcessingStatus.PROCESSING, 10);
    assertEquals(1, relevancesByStatus.size());
  }


}
