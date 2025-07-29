package vn.chuot96.databaseConnectorAPI.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class NosqlRequestDTO extends RequestDTO {
    private String collection;
    private Map<String, Object> data;    // insert or update
    private Map<String, Object> filter;  // update or delete
}
