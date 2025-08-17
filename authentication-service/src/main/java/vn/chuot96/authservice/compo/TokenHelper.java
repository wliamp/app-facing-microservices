package vn.chuot96.authservice.compo;

import io.wliamp.token.data.Type;
import io.wliamp.token.util.ExternalToken;
import io.wliamp.token.util.InternalToken;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;

@Component
@RequiredArgsConstructor
public class TokenHelper {
    private final InternalToken internal;

    private final ExternalToken external;

    public Mono<Map<String, Object>> getFacebookInfo(String token) {
        return external.getFacebook()
                .verify(token)
                .flatMap(valid -> valid ? external.getFacebook().getInfo(token) : Mono.empty());
    }

    public Mono<Map<String, Object>> getGoogleInfo(String token) {
        return external.getGoogle()
                .verify(token)
                .flatMap(valid -> valid ? external.getGoogle().getInfo(token) : Mono.empty());
    }

    public Mono<Map<String, Object>> getZaloInfo(String token) {
        return external.getZalo()
                .verify(token)
                .flatMap(valid -> valid ? external.getZalo().getInfo(token) : Mono.empty());
    }

    public Mono<UserToken> issueGuestToken(String sub, Map<String, Object> extraClaims) {
        Mono<String> accessMono = internal.issue(sub, Type.ACCESS, 3600, extraClaims);
        Mono<String> refreshMono = internal.issue(sub, Type.REFRESH, 2592000);
        return Mono.zip(accessMono, refreshMono).map(tuple -> new UserToken(tuple.getT1(), tuple.getT2()));
    }

    public Mono<String> getFacebookId(String token) {
        return getFacebookInfo(token).map(info -> info.get("id").toString());
    }

    public Mono<String> getGoogleSub(String token) {
        return getGoogleInfo(token).map(info -> info.get("sub").toString());
    }

    public Mono<String> getZaloId(String token) {
        return getZaloInfo(token).map(info -> info.get("id").toString());
    }

    public Mono<UserToken> issueTokenByFacebook(String token, Map<String, Object> extraClaims) {
        return getFacebookInfo(token).flatMap(info -> {
            String sub = "facebook:" + getFacebookId(token);
            return issueGuestToken(sub, extraClaims);
        });
    }

    public Mono<UserToken> issueTokenByGoogle(String token, Map<String, Object> extraClaims) {
        return getGoogleInfo(token).flatMap(info -> {
            String sub = "google:" + getGoogleSub(token);
            return issueGuestToken(sub, extraClaims);
        });
    }

    public Mono<UserToken> issueTokenByZalo(String token, Map<String, Object> extraClaims) {
        return getZaloInfo(token).flatMap(info -> {
            String sub = "zalo:" + getZaloId(token);
            return issueGuestToken(sub, extraClaims);
        });
    }
}
