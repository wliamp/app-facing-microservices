package io.wliamp.cko.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record Request(
        String amount,
        String ipAddress,
        Map<String, String> metadata
) {
}
