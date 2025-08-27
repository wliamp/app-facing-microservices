package io.wliamp.pay.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record ListenEvent(
        String orderId,
        BigDecimal amount,
        String currency,
        String method,
        String ipAddress,
        String description,
        Map<String, String> metadata
) {
}
