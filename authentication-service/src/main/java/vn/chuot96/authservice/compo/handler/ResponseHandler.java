package vn.chuot96.authservice.compo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.Tokens;

@Component
public class ResponseHandler {
    public Mono<ServerResponse> buildTokenResponse(Tokens token) {
        return ServerResponse.ok()
                .header("X-Access-Token", token.access())
                .header("X-Refresh-Token", token.refresh())
                .build();
    }
}
