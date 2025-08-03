package vn.chuot96.dbconnapi.util;

import static vn.chuot96.dbconnapi.constant.SqlConfigParam.DEFAULT_SELECT_LIMIT;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.chuot96.dbconnapi.component.SqlConnectionPool;
import vn.chuot96.dbconnapi.constant.SqlDeniedCmd;
import vn.chuot96.dbconnapi.dto.SqlRequestDTO;

@RequiredArgsConstructor
public class SqlHandler {

    private final SqlConnectionPool sqlConnectionPool;

    public ResponseEntity<?> execute(String driverClass, String jdbcUrl, SqlRequestDTO request) {
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
        String rawQuery = request.getQuery().trim();
        String loweredQuery = rawQuery.toLowerCase(Locale.ROOT);
        Optional<String> deniedCommand = SqlDeniedCmd.findFirstMatch(loweredQuery);
        if (deniedCommand.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",
                            "Forbidden SQL command detected",
                            "query",
                            rawQuery,
                            "detectedCommand",
                            deniedCommand.get()));
        }
        try {
            DataSource dataSource =
                    sqlConnectionPool.getDataSource(driverClass, jdbcUrl, request.getUsername(), request.getPassword());
            long start = System.currentTimeMillis();
            try (Connection conn = dataSource.getConnection();
                    Statement stmt = conn.createStatement()) {
                if (isSelectQuery(loweredQuery)) {
                    String safeQuery = appendLimitIfMissing(rawQuery, loweredQuery);
                    try (ResultSet rs = stmt.executeQuery(safeQuery)) {
                        List<Map<String, Object>> result = convert(rs);
                        long duration = System.currentTimeMillis() - start;
                        return ResponseEntity.ok(
                                Map.of("rows", result, "count", result.size(), "durationMs", duration));
                    }
                } else {
                    int affectedRows = stmt.executeUpdate(rawQuery);
                    long duration = System.currentTimeMillis() - start;
                    return ResponseEntity.ok(Map.of(
                            "affectedRows",
                            affectedRows,
                            "message",
                            "Operation executed successfully",
                            "durationMs",
                            duration));
                }
            }

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "SQL Error", "details", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Unexpected error", "details", e.getMessage()));
        }
    }

    private boolean isSelectQuery(String query) {
        return Pattern.compile("^\\s*select\\b", Pattern.CASE_INSENSITIVE)
                .matcher(query)
                .find();
    }

    private String appendLimitIfMissing(String rawQuery, String loweredQuery) {
        if (Pattern.compile("\\blimit\\b").matcher(loweredQuery).find()) {
            return rawQuery;
        }
        return rawQuery + " LIMIT " + DEFAULT_SELECT_LIMIT;
    }

    private List<Map<String, Object>> convert(ResultSet rs) throws SQLException {
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
