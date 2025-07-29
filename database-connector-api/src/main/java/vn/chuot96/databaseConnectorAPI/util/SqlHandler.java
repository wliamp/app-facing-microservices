package vn.chuot96.databaseConnectorAPI.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.chuot96.databaseConnectorAPI.dto.SqlRequestDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlHandler {
    public static ResponseEntity<?> execute(String driverClass, String jdbcUrl, SqlRequestDTO request) {
        try {
            Class.forName(driverClass);
            try (Connection conn = DriverManager.getConnection(jdbcUrl, request.getUsername(), request.getPassword());
                 Statement stmt = conn.createStatement()) {

                String query = request.getQuery().trim().toLowerCase();

                if (query.startsWith("select")) {
                    ResultSet rs = stmt.executeQuery(request.getQuery());
                    List<Map<String, Object>> result = convert(rs);
                    return ResponseEntity.ok(result);
                } else {
                    int affected = stmt.executeUpdate(request.getQuery());
                    return ResponseEntity.ok(Map.of(
                            "affectedRows", affected,
                            "message", "Operation successful"
                    ));
                }

            }
        } catch (ClassNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
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
}
