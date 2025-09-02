package io.wliamp.cko.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("orders")
public class Order {
    @Id
    private long id;
}
