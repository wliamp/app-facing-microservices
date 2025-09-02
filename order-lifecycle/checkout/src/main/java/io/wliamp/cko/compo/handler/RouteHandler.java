package io.wliamp.cko.compo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RouteHandler {
    public Mono<ServerResponse> checkout(ServerRequest serverRequest) {
    }
}
