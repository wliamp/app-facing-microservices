package vn.chuot96.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("accounts")
public class Acc {
    @Id
    private Long id;

    private String code;
    private Boolean status;
    private String credential;
}
