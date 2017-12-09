package ifood.score.menu;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MenuRelevanceRepository {

  private Set<MenuRelevance> relevances = Collections.synchronizedSet(new HashSet<>());

  public void addRelevances(List<MenuRelevance> relevancesCreated) {
    this.relevances.addAll(relevancesCreated);
  }

  public void updateRelevance(MenuRelevance relevance) {
    this.relevances.remove(relevance);
    this.relevances.add(relevance);
  }

  public Set<MenuRelevance> getRelevances(int limit) {
    return this.relevances.stream().limit(limit).collect(Collectors.toSet());
  }

  public void removeRelevances(Set<MenuRelevance> relevancesRemoved) {
    this.relevances.removeAll(relevancesRemoved);
  }

}
