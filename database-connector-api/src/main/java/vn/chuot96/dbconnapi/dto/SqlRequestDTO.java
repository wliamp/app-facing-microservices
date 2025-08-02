package vn.chuot96.dbconnapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SqlRequestDTO extends DbRequestDTO {
    String query;
}

