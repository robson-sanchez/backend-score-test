package ifood.score.category;

import ifood.score.model.ProcessingStatus;
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

  public void updateRelevances(Set<CategoryRelevance> relevances) {
    Set<CategoryRelevance> newSet = this.relevances.stream()
        .filter(item -> !relevances.contains(item))
        .collect(Collectors.toSet());

    newSet.addAll(relevances);

    this.relevances = newSet;
  }

  public Set<CategoryRelevance> getRelevancesByStatus(ProcessingStatus status, int limit) {
    return this.relevances.stream()
        .filter(r -> status.equals(r.getProcessingStatus()))
        .limit(limit)
        .collect(Collectors.toSet());
  }

  public void removeRelevance(CategoryRelevance relevance) {
    this.relevances.remove(relevance);
  }
}
