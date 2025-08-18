package vn.chuot96.authservice.compo.helper;

import java.util.Map;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;

public interface PartyHelper {
    Mono<String> getSubject(String token);

    Mono<UserToken> issueToken(String token, Map<String, Object> claims);
}
