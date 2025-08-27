package io.wliamp.pay.dto;

import lombok.Builder;

import java.util.Map;

@Builder
public record SendEvent(
        String orderId,
        String provider,
        String status,
        String redirectUrl,
        String transactionId,
        Map<String, String> metadata,
        String errorMessage
) {
}
