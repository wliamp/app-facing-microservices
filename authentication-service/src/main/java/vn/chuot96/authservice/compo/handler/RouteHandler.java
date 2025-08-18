package vn.chuot96.authservice.compo.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.service.authenticate.AuthenticateService;

@Component
@RequiredArgsConstructor
public class RouteHandler {
    private final AuthenticateService authenticateService;

    public Mono<ServerResponse> guest(ServerRequest request) {
        return authenticateService.guestLogin()
                .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        String party = request.pathVariable("party");
        return request.bodyToMono(String.class)
                .flatMap(external -> authenticateService.loginWithoutBearer(party, external))
                .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    public Mono<ServerResponse> relog(ServerRequest request) {
        String internal = request.headers().firstHeader("Authorization");
        if (internal == null) {
            return ServerResponse.badRequest().build();
        }
        return authenticateService.loginWithBearer(internal)
                .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    public Mono<ServerResponse> link(ServerRequest request) {
        String internal = request.headers().firstHeader("Authorization");
        String party = request.pathVariable("party");
        return request.bodyToMono(String.class)
                .flatMap(external -> {
                    assert internal != null;
                    return authenticateService.linkAccount(internal, party, external);
                })
                .flatMap(token -> ServerResponse.ok().bodyValue(token));
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        String internal = request.headers().firstHeader("Authorization");
        assert internal != null;
        return authenticateService.logout(internal)
                .then(ServerResponse.ok().build());
    }
}
