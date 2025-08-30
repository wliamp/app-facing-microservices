package io.wliamp.pay.dto;

import lombok.Builder;

@Builder
public record SaleRequest(
        String orderId,
        String method,
        String amount,
        String description,
        String ipAddress
) {
}
