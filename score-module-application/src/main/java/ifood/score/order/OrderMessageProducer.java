package ifood.score.order;

import ifood.score.category.CategoryRelevance;
import ifood.score.menu.MenuRelevance;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles checkout order event.
 *
 * Created by robson on 03/12/17.
 */
@Component
public class OrderMessageProducer {

  private final JmsTemplate jmsTemplate;

  public OrderMessageProducer(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  public void sendEvents(Order order, OrderStatus status) {
    sendMenuItemsBulkMessage(order, status);
    sendCategoriesBulkMessage(order, status);
  }

  private void sendMenuItemsBulkMessage(Order order, OrderStatus status) {
    List<MenuRelevance> menuItems = order.getItemsRelevance().entrySet().stream().map(entry -> {
      MenuRelevance relevance = new MenuRelevance();

      relevance.setOrder(order.getUuid());
      relevance.setMenu(entry.getKey());
      relevance.setRelevance(entry.getValue());
      relevance.setOrderStatus(status);

      return relevance;
    }).collect(Collectors.toList());

    this.jmsTemplate.convertAndSend("menu-relevance", menuItems);
  }

  private void sendCategoriesBulkMessage(Order order, OrderStatus status) {
    List<CategoryRelevance> categories = order.getCategoriesRelevance().entrySet()
        .stream().map(entry -> {
          CategoryRelevance relevance = new CategoryRelevance();

          relevance.setOrder(order.getUuid());
          relevance.setCategory(entry.getKey());
          relevance.setRelevance(entry.getValue());
          relevance.setOrderStatus(status);

          return relevance;
        }).collect(Collectors.toList());

    this.jmsTemplate.convertAndSend("category-relevance", categories);
  }

}
