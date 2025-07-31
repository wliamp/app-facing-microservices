package vn.chuot96.clientgwapi.component;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Configuration
public class LoggingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = UUID.randomUUID().toString();
        exchange.getRequest().mutate()
                .headers(h -> h.add("X-Trace-Id", traceId)).build();
        System.out.println("[TRACE " + traceId + "] "
                + exchange.getRequest().getMethod() + " "
                + exchange.getRequest().getURI());
        return chain.filter(exchange);
    }
}
