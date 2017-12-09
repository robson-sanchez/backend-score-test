package ifood.score.category;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryRelevanceRepository {

  private Set<CategoryRelevance> relevances = Collections.synchronizedSet(new HashSet<>());

  public void addRelevances(List<CategoryRelevance> relevancesCreated) {
    this.relevances.addAll(relevancesCreated);
  }

  public void updateRelevance(CategoryRelevance relevance) {
    this.relevances.remove(relevance);
    this.relevances.add(relevance);
  }

  public Set<CategoryRelevance> getRelevances(int limit) {
    return this.relevances.stream().limit(limit).collect(Collectors.toSet());
  }

  public void removeRelevances(Set<CategoryRelevance> relevancesRemoved) {
    this.relevances.removeAll(relevancesRemoved);
  }

}
