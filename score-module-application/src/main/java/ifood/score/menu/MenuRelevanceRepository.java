package ifood.score.menu;

import ifood.score.RelevanceRepository;
import ifood.score.model.ProcessingStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MenuRelevanceRepository extends RelevanceRepository<MenuRelevance> {

}
