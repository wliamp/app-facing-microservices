package io.wliamp.pay.entity;

import io.wliamp.pay.constant.TransactionStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@Table("payments")
public class Payment {
    @Id
    private Long id;

    private String orderId;
    private String method;
    private String description;
    private String ipAddress;
    private BigDecimal amount;
}
