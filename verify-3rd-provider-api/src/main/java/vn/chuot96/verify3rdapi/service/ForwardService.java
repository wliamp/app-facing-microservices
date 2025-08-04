package vn.chuot96.verify3rdapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rdapi.component.ExternalFileHelper;
import vn.chuot96.verify3rdapi.component.ForwardHelper;
import vn.chuot96.verify3rdapi.dto.UserDTO;

@Service
@RequiredArgsConstructor
public class ForwardService {
    private final ForwardHelper forwardHelper;

    private final ExternalFileHelper externalFileHelper;

    public Mono<?> forwardAuthServiceLogin(UserDTO user) {
        return forwardHelper.post(
                "authentication-service",
                "/auth/login",
                externalFileHelper.getString("HeaderName"),
                externalFileHelper.getString("HeaderValueAuth"),
                user,
                UserDTO.class);
    }

    public Mono<String> forwardJwtIssApiAccessRefresh() {
        return forwardHelper.post(
                "token-issuer-api",
                "/issuer/tokens",
                externalFileHelper.getString("HeaderName"),
                externalFileHelper.getString("HeaderValueJwtIss"),
                "",
                String.class);
    }
}
