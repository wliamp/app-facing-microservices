package io.wliamp.pay.dto;

import lombok.Builder;

@Builder
public record SaleRequest(
        String orderId,
        String amount,
        String description,
        String ipAddress
) {
}
