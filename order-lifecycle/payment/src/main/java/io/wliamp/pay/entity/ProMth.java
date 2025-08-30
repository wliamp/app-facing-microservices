package io.wliamp.pay.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("provider_method")
public class ProMth {
    private long provider;
    private long method;
    private String name;
}
