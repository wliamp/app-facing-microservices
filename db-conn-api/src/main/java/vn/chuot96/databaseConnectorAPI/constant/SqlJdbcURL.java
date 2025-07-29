package vn.chuot96.databaseConnectorAPI.constant;

import lombok.Getter;
import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.databaseConnectorAPI.dto.SqlRequestDTO;

@Getter
public enum SqlJdbcURL {

    MYSQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s"),

    POSTGRES("org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s"),

    MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%s;databaseName=%s");

    // --> more SQL type here

    private final String driverClass;
    private final String pattern;

    SqlJdbcURL(String driverClass, String pattern) {
        this.driverClass = driverClass;
        this.pattern = pattern;
    }

    public String setJdbcUrl(@RequestBody SqlRequestDTO request) {
        return String.format(pattern, request.getHost(), request.getPort(), request.getDatabase());
    }

}
