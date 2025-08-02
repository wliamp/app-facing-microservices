package vn.chuot96.dbconnapi.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class NosqlRequestDTO extends DbRequestDTO{
    private String collection;
    private Map<String, Object> data;
    private Map<String, Object> filter;
}
