package vn.chuot96.dbconnapi.constant;

import lombok.Getter;
import vn.chuot96.dbconnapi.dto.SqlRequestDTO;

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

    public String setJdbcUrl(SqlRequestDTO request) {
        return String.format(pattern, request.getHost(), request.getPort(), request.getDatabase());
    }

}
