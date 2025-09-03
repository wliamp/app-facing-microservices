package io.wliamp.cko.compo.handler;

import io.github.wliamp.token.util.TokenUtil;
import io.wliamp.cko.util.Extractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TokenHandler {
    private final TokenUtil tokenUtil;

    public Mono<String> getUserId(String token) {
        return tokenUtil.getClaims(token).map(t -> Extractor.extractToken(t.get("sub").toString(), 20));
    }
}
