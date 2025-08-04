package vn.chuot96.verify3rdapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rdapi.component.ExternalFileReader;
import vn.chuot96.verify3rdapi.component.ForwardHelper;
import vn.chuot96.verify3rdapi.dto.RequestDTO;
import vn.chuot96.verify3rdapi.dto.UserDTO;

@Service
@RequiredArgsConstructor
public class ForwardService {
    private final ForwardHelper forwardHelper;

    private final ExternalFileReader externalFileReader;

    public Mono<?> forwardAuthServiceLogin(RequestDTO request, UserDTO user) {
        return forwardHelper.post(
                "authentication-service-" + request.appId(),
                "/auth/login",
                externalFileReader.string("AuthLoginHeaderName"),
                externalFileReader.string("AuthLoginHeaderValue"),
                user,
                UserDTO.class);
    }

    public Mono<String> forwardJwtIssApiAccessRefresh() {
        return forwardHelper.post(
                "token-issuer-api",
                "/issue/access-refresh",
                externalFileReader.string("JwtIssARHeaderName"),
                externalFileReader.string("JwtIssARHeaderValue"),
                "",
                String.class);
    }
}
