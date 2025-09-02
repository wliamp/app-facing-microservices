package io.wliamp.pay.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("provider_currency")
public class ProCur {
    private long provider;
    private long currency;
    private String name;
}
