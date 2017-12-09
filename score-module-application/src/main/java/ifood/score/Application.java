package ifood.score;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import ifood.score.category.CategoryHandler;
import ifood.score.menu.MenuHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = "ifood.score")
@EnableJms
public class Application {

  public static void main(String[] args) throws Exception {
    // Enable ActiveMQ to serialize objects from all packages
    System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

    SpringApplication.run(Application.class);
  }

  @Bean
  public RouterFunction<ServerResponse> monoRouterFunction(EchoHandler echoHandler,
      MenuHandler menuHandler, CategoryHandler categoryHandler) {
    return route(POST("/echo"), echoHandler::echo)
        .andRoute(GET("/v1/score/category/search"), categoryHandler::retrieveScoreByParameter)
        .andRoute(GET("/v1/score/category/{category}"), categoryHandler::getScore)
        .andRoute(GET("/v1/score/menu/search"), menuHandler::retrieveScoreByParameter)
        .andRoute(GET("/v1/score/menu/{menu}"), menuHandler::getScore);
  }
}
