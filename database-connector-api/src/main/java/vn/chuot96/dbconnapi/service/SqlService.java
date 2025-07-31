package vn.chuot96.dbconnapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.chuot96.dbconnapi.dto.SqlRequestDTO;
import vn.chuot96.dbconnapi.util.SqlHandler;

import static vn.chuot96.dbconnapi.constant.SqlJdbcURL.*;
import static vn.chuot96.dbconnapi.constant.SqlJdbcURL.MSSQL;

@Service
@RequiredArgsConstructor
public class SqlService {

    private final SqlHandler handler;

    public ResponseEntity<?> mysqlQuery(SqlRequestDTO request) {
        return handler.execute(MYSQL.getDriverClass(), MYSQL.setJdbcUrl(request), request);
    }

    public ResponseEntity<?> postgresQuery(SqlRequestDTO request) {
        return handler.execute(POSTGRES.getDriverClass(), POSTGRES.setJdbcUrl(request), request);
    }

    public ResponseEntity<?> mssqlQuery(SqlRequestDTO request) {
        return handler.execute(MSSQL.getDriverClass(), MSSQL.setJdbcUrl(request), request);
    }

    // --> more SQL type here

}
