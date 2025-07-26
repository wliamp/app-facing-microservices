package vn.chuot96.dbConnAPI.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.dbConnAPI.dto.SqlRequestDTO;
import vn.chuot96.dbConnAPI.util.SqlConverter;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Service
public class SqlService {
    public ResponseEntity<?> executeQuery(@RequestBody SqlRequestDTO request) {
        String jdbcUrl;
        String driverClass;
        String host = request.getHost();
        String port = request.getPort();
        String schema = request.getDatabase();

        switch (request.getType()) {
            case MYSQL -> {
                driverClass = "com.mysql.cj.jdbc.Driver";
                jdbcUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, schema);
            }
            case POSTGRES -> {
                driverClass = "org.postgresql.Driver";
                jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, schema);
            }
            case MSSQL -> {
                driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                jdbcUrl = String.format("jdbc:sqlserver://%s:%s;databaseName=%s", host, port, schema);
            }
            default -> {
                return ResponseEntity.badRequest().body("Unsupported Database Type: " + request.getType());
            }
        }

        try {
            Class.forName(driverClass);
            try (Connection conn = DriverManager.getConnection(jdbcUrl, request.getUsername(), request.getPassword())) {
                Statement stmt = conn.createStatement();
                String query = request.getQuery().trim().toLowerCase();

                if (query.startsWith("select")) {
                    ResultSet rs = stmt.executeQuery(request.getQuery());
                    List<Map<String, Object>> result = SqlConverter.convert(rs);
                    return ResponseEntity.ok(result);
                } else {
                    int affected = stmt.executeUpdate(request.getQuery());
                    Map<String, Object> response = Map.of("affectedRows", affected,
                            "message", "Operation executed successfully");
                    return ResponseEntity.ok(response);
                }
            }
        } catch (ClassNotFoundException e) {
            return ResponseEntity.status(500).body("Driver Not Found: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("SQL Error: " + e.getMessage());
        }
    }
}
