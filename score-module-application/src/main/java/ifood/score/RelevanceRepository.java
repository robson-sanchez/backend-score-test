package ifood.score;

import ifood.score.model.ProcessingStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by robson on 10/12/17.
 */
public abstract class RelevanceRepository<T extends Relevance> {

  private Set<T> relevances = Collections.synchronizedSet(new HashSet<>());

  public void addRelevances(List<T> relevancesCreated) {
    this.relevances.addAll(relevancesCreated);
  }

  public void updateRelevances(Set<T> relevances) {
    Set<T> newSet = this.relevances.stream()
        .filter(item -> !relevances.contains(item))
        .collect(Collectors.toSet());

    newSet.addAll(relevances);

    this.relevances = newSet;
  }

  public void updateRelevance(T relevance) {
    this.relevances.remove(relevance);
    this.relevances.add(relevance);
  }

  public Set<T> getRelevancesByStatus(ProcessingStatus status, int limit) {
    return this.relevances.stream()
        .filter(r -> status.equals(r.getProcessingStatus()))
        .limit(limit)
        .collect(Collectors.toSet());
  }

  public void removeRelevance(T relevance) {
    this.relevances.remove(relevance);
  }

}
