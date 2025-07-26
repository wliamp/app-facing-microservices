package vn.chuot96.dbConnAPI.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class NosqlRequestDTO extends RequestDTO {
    private String collection;
    private String operation; // insert, update, delete, find
    private Map<String, Object> data;    // insert or update
    private Map<String, Object> filter;  // update or delete
}
