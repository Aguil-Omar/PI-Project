package com.pi_dev.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static HikariDataSource dataSource;
    private static DataSource instance;

    private DataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/eventopia1");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(20);          // Increased pool size
        config.setMinimumIdle(5);              // Minimum idle connections
        config.setConnectionTimeout(60000);    // Increased timeout
        config.setIdleTimeout(600000);         // 10 minutes
        config.setMaxLifetime(1800000);        // 30 minutes
        config.setConnectionTestQuery("SELECT 1"); // Validation query
        config.setLeakDetectionThreshold(60000);   // Leak detection

        dataSource = new HikariDataSource(config);
    }

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSource.class) {
                if (instance == null) {
                    instance = new DataSource();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        if (connection == null || connection.isClosed()) {
            throw new SQLException("La connexion à la base de données est fermée ou indisponible.");
        }
        return connection;
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}