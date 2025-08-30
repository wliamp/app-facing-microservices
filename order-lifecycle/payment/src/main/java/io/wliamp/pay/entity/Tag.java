package io.wliamp.pay.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("tags")
public class Tag {
    @Id
    private long id;

    private String code;
    private String name;
}
