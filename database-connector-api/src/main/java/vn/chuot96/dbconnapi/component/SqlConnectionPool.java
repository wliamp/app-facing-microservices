package vn.chuot96.dbconnapi.component;

import static vn.chuot96.dbconnapi.constant.SqlConfigParam.*;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;

@Component
public class SqlConnectionPool {
    private final ConcurrentHashMap<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    public DataSource getDataSource(String driverClass, String jdbcUrl, String username, String password) {
        String key = jdbcUrl + ":" + username;
        return dataSourceMap.computeIfAbsent(key, k -> {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driverClass);
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(MAXIMUM_POOL_SIZE.ordinal());
            config.setConnectionTimeout(CONNECTION_TIMEOUT.getValue());
            config.setIdleTimeout(IDLE_TIMEOUT.getValue());
            config.setMaxLifetime(MAX_LIFE_TIME.getValue());
            return new HikariDataSource(config);
        });
    }
}
