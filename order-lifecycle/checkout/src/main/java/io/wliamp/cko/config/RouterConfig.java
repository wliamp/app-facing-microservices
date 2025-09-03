package io.wliamp.cko.config;

import io.wliamp.cko.compo.handler.RouteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {
    private final RouteHandler routeHandler;

    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return route().nest(path("/cko"), () -> route()
//                        .POST("/authorize", routeHandler::authorize)
//                        .POST("/capture", routeHandler::capture)
                        .POST("/sale/{method}/{currency}/{provider}", routeHandler::sale)
//                        .POST("/refund", routeHandler::refund)
//                        .POST("/cancel", routeHandler::cancel)
                        .build())
                .build();
    }
}
