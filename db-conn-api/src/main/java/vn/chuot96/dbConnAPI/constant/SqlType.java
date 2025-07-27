package vn.chuot96.dbConnAPI.constant;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.dbConnAPI.dto.SqlRequestDTO;

public enum SqlType {

    MYSQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s"),

    POSTGRES("org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s"),

    MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%s;databaseName=%s");

    @Getter
    private String driverClass;
    private String jdbcUrl;

    SqlType(String driverClass, String jdbcUrl) {
    }

    public String getJdbcUrl(@RequestBody SqlRequestDTO request) {
        return String.format(jdbcUrl, request.getHost(), request.getPort(), request.getDatabase());
    }

}
