package vn.chuot96.databaseConnectorAPI.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SqlRequestDTO extends RequestDTO {
    private String query;
}

