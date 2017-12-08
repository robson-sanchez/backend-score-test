package ifood.score.menu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuRelevanceRepository {

  private static final MenuRelevanceRepository INSTANCE = new MenuRelevanceRepository();

  private Set<MenuRelevance> relevances = new HashSet<>();

  public static MenuRelevanceRepository getInstance() {
    return INSTANCE;
  }

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
