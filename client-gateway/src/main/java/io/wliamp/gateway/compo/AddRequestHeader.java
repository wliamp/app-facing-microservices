package io.wliamp.gateway.compo;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AddRequestHeader implements GlobalFilter, Ordered {
    private final ExternalFileReader reader;

    public AddRequestHeader(ExternalFileReader reader) {
        this.reader = reader;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header(reader.string("InternalHeaderName"), reader.string("GatewayHeaderValue"))
                .build();
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();
        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
