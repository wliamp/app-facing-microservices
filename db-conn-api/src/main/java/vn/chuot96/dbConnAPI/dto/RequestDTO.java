package vn.chuot96.dbConnAPI.dto;

import lombok.Data;

@Data
public class RequestDTO {
    private String type;
    private String host;
    private String port;
    private String schema;
    private String username;
    private String password;
    private String query;
}
