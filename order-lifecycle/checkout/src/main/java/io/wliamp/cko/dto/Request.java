package io.wliamp.cko.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record Request(
        String userId,
        String method,
        String currency,
        String provider,
        String amount,
        String ipAddress,
        Map<String, String> metadata
) {
}
