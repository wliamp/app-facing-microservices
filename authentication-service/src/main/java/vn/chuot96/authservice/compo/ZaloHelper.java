package vn.chuot96.authservice.compo;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;

@Component("zl")
@RequiredArgsConstructor
public class ZaloHelper implements PartyHelper {
    private final TokenHelper helper;

    @Override
    public Mono<String> getSubject(String token) {
        return helper.getZaloId(token);
    }

    @Override
    public Mono<UserToken> issueToken(String token, Map<String, Object> claims) {
        return helper.issueTokenByZalo(token, claims);
    }
}
