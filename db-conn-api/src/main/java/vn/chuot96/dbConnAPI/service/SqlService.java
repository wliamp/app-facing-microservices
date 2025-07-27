package vn.chuot96.dbConnAPI.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.dbConnAPI.constant.SqlType;
import vn.chuot96.dbConnAPI.dto.SqlRequestDTO;
import vn.chuot96.dbConnAPI.util.SqlHandler;

@Service
public class SqlService {

    public static ResponseEntity<?> mysqlQuery(@RequestBody SqlRequestDTO request) {
        return SqlHandler.execute(SqlType.MYSQL.getDriverClass(), SqlType.MYSQL.getJdbcUrl(request), request);
    }

    public static ResponseEntity<?> postgresQuery(@RequestBody SqlRequestDTO request) {
        return SqlHandler.execute(SqlType.POSTGRES.getDriverClass(), SqlType.POSTGRES.getJdbcUrl(request), request);
    }

    public static ResponseEntity<?> mssqlQuery(@RequestBody SqlRequestDTO request) {
        return SqlHandler.execute(SqlType.MSSQL.getDriverClass(), SqlType.MSSQL.getJdbcUrl(request), request);
    }

}
