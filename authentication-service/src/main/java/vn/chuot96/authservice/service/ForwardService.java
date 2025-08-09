package vn.chuot96.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.ExternalFileReader;
import vn.chuot96.authservice.compo.ForwardHelper;
import vn.chuot96.authservice.dto.AuthRequest;
import vn.chuot96.authservice.dto.UserInfo;

@Service
@RequiredArgsConstructor
public class ForwardService {
    private final ExternalFileReader externalFileReader;

    private final ForwardHelper forwardHelper;

    public Mono<?> forwardJwtIss(UserInfo user) {
        return forwardHelper.post(
                "token-issuer",
                "/issue/access-refresh",
                externalFileReader.string("AuthForwardJwtJssARHeaderValue"),
                user,
                UserInfo.class);
    }

    public Mono<?> forwardRCache(UserInfo request) {
        return forwardHelper.post(
                "redis-cache",
                "/cache",
                externalFileReader.string("AuthForwardRCacheHeaderValue"),
                request,
                AuthRequest.class);
    }
}
