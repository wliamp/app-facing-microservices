package vn.chuot96.dbConnAPI.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.chuot96.dbConnAPI.constant.SqlType;

@EqualsAndHashCode(callSuper = true)
@Data
public class SqlRequestDTO extends RequestDTO {
    private SqlType type;
    private String query;
}

