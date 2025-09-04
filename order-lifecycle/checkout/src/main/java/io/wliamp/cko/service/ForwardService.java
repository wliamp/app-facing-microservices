package io.wliamp.cko.service;

import io.wliamp.cko.compo.handler.ForwardHandler;
import io.wliamp.cko.compo.handler.TokenHandler;
import io.wliamp.cko.dto.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ForwardService {
    @Value("${token.header.name}")
    private final String headerName;

    @Value("${token.header.value}")
    private final String headerValue;

    private final ForwardHandler forwardHandler;

    private final TokenHandler tokenHandler;

    public Mono<String> fwPayment(String token, String action, MultiValueMap<String, String> params, Request request) {
        return forwardHandler.post(
                "pay",
                "/" + action,
                Map.of(headerName, headerValue),
                Map.of(
                        "userId", tokenHandler.getUserId(token),
                        "method", params.get("method"),
                        "currency", params.get("currency"),
                        "provider", params.get("provider"),
                        "amount", request.amount(),
                        "ipAddress", request.ipAddress(),
                        "metadata", request.metadata()
                ),
                String.class
        );
    }
}
