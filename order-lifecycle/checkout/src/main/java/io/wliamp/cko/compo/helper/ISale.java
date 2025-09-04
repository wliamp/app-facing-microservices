package io.wliamp.cko.compo.helper;

import io.wliamp.cko.dto.Request;
import reactor.core.publisher.Mono;

public interface ISale {
    Mono<Boolean> execute(String token, Request request);
}
