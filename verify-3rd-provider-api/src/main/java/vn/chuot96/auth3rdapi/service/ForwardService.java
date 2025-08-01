package vn.chuot96.auth3rdapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.auth3rdapi.component.ForwardHelper;
import vn.chuot96.auth3rdapi.dto.UserDTO;

@Service
@RequiredArgsConstructor
public class ForwardService {

    private final ForwardHelper forwardHelper;

    public Mono<?> forwardAuthService(UserDTO user) {
        return forwardHelper.post("authentication-service",
                        "/auth",
                        "111-111-111",
                        user,
                        UserDTO.class);
    }

    public Mono<String> forwardJwtJssApiTokens() {
        return forwardHelper.post("token-issuer-api",
                        "/issuer/tokens",
                        "222-222-222",
                        "",
                        String.class);
    }

    public Mono<?> forwardJwtJssApiAccess() {
        return forwardHelper.post("token-issuer-api",
                        "/issuer/access",
                        "333-333-333",
                        "",
                        Void.class);
    }

}
