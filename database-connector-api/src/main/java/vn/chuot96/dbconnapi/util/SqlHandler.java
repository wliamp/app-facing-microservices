package vn.chuot96.dbconnapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.chuot96.dbconnapi.dto.SqlRequestDTO;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

import static vn.chuot96.dbconnapi.constant.SqlJdbcURL.*;

public class SqlHandler {
    public static ResponseEntity<?> execute(String driverClass, String jdbcUrl, SqlRequestDTO request) {
        if (driverClass == null
                || driverClass.isBlank()
                || jdbcUrl == null
                || jdbcUrl.isBlank()
                || request == null
                || request.getQuery() == null
                || request.getQuery().isBlank()
                || request.getUsername() == null
                || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }

        try {
            Class.forName(driverClass);

            try (Connection conn = DriverManager.getConnection(jdbcUrl, request.getUsername(), request.getPassword());
                 Statement stmt = conn.createStatement()) {

                String rawQuery = request.getQuery().trim();
                String loweredQuery = rawQuery.toLowerCase(Locale.ROOT);

                if (isSelectQuery(loweredQuery)) {
                    try (ResultSet rs = stmt.executeQuery(rawQuery)) {
                        List<Map<String, Object>> result = convert(rs);
                        return ResponseEntity.ok(Map.of(
                                "rows", result,
                                "count", result.size()
                        ));
                    }
                } else {
                    int affectedRows = stmt.executeUpdate(rawQuery);
                    return ResponseEntity.ok(Map.of(
                            "affectedRows", affectedRows,
                            "message", "Operation executed successfully"
                    ));
                }

            }
        } catch (ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "JDBC Driver not found",
                            "details", e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "SQL Error",
                            "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error",
                            "details", e.getMessage()));
        }
    }


    public static ResponseEntity<?> mysqlQuery(SqlRequestDTO request) {
        return execute(MYSQL.getDriverClass(), MYSQL.setJdbcUrl(request), request);
    }

    public static ResponseEntity<?> postgresQuery(SqlRequestDTO request) {
        return execute(POSTGRES.getDriverClass(), POSTGRES.setJdbcUrl(request), request);
    }

    public static ResponseEntity<?> mssqlQuery(SqlRequestDTO request) {
        return execute(MSSQL.getDriverClass(), MSSQL.setJdbcUrl(request), request);
    }

    private static List<Map<String, Object>> convert(ResultSet rs) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnLabel(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

    private static boolean isSelectQuery(String query) {
        return Pattern.compile("^\\s*select\\b", Pattern.CASE_INSENSITIVE).matcher(query).find();
    }

}
