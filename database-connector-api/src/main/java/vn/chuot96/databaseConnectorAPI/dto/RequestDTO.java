package vn.chuot96.databaseConnectorAPI.dto;

import lombok.Data;

@Data
public class RequestDTO {
    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
}
