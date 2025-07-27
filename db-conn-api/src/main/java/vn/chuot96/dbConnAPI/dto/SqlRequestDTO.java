package vn.chuot96.dbConnAPI.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SqlRequestDTO extends RequestDTO {
    private String query;
}

