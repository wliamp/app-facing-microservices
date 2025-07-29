package vn.chuot96.databaseConnectorAPI.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.databaseConnectorAPI.dto.SqlRequestDTO;
import vn.chuot96.databaseConnectorAPI.util.SqlHandler;

import static vn.chuot96.databaseConnectorAPI.constant.SqlJdbcURL.*;

@Service
public class SqlService {

    public static ResponseEntity<?> mysqlQuery(@RequestBody SqlRequestDTO request) {
        return SqlHandler.execute(MYSQL.getDriverClass(), MYSQL.setJdbcUrl(request), request);
    }

    public static ResponseEntity<?> postgresQuery(@RequestBody SqlRequestDTO request) {
        return SqlHandler.execute(POSTGRES.getDriverClass(), POSTGRES.setJdbcUrl(request), request);
    }

    public static ResponseEntity<?> mssqlQuery(@RequestBody SqlRequestDTO request) {
        return SqlHandler.execute(MSSQL.getDriverClass(), MSSQL.setJdbcUrl(request), request);
    }

}
