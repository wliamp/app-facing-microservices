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

    public Mono<UserDTO> forwardAuthService(UserDTO user) {
        return forwardHelper.post("authentication-service", "/auth", user, UserDTO.class);
    }

    public Mono<UserDTO> forwardJwtJssApi(UserDTO user) {
        return forwardHelper.post("token-issuer-api", "/jwt", user, UserDTO.class);
    }

}
