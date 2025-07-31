package vn.chuot96.dbconnapi.dto;

import lombok.Data;

@Data
public class RequestDTO {
    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
}
