package vn.chuot96.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.ForwardHelper;
import vn.chuot96.authservice.dto.AuthRequest;
import vn.chuot96.authservice.dto.UserInfo;

@Service
@RequiredArgsConstructor
public class ForwardService {
    private final ForwardHelper forwardHelper;

    public Mono<?> forwardJwtIssApi(UserInfo user) {
        return forwardHelper.post("token-issuer-api", "/issue/access-refresh", user, UserInfo.class);
    }

    public Mono<?> forwardRCache(AuthRequest request) {
        return forwardHelper.post("redis-cache", "/cache", request, AuthRequest.class);
    }
}
