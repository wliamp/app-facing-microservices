package vn.chuot96.dbConnAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.chuot96.dbConnAPI.dto.SqlRequestDTO;
import vn.chuot96.dbConnAPI.util.SqlConverter;

import java.sql.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sql")
public class SqlController {
    @PostMapping("/query")
    public ResponseEntity<?> query(@RequestBody SqlRequestDTO request) {
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
                return ResponseEntity.badRequest().body("Unsupported DB type: " + request.getType());
            }
        }

        try {
            Class.forName(driverClass);
            try (Connection conn = DriverManager.getConnection(jdbcUrl, request.getUsername(), request.getPassword())) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(request.getQuery());
                List<Map<String, Object>> result = SqlConverter.convert(rs);
                return ResponseEntity.ok(result);
            }
        } catch (ClassNotFoundException e) {
            return ResponseEntity.status(500).body("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("SQL error: " + e.getMessage());
        }
    }
}
