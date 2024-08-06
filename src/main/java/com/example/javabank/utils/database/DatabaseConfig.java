package com.example.javabank.utils.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final int MAX_RETRIES = 50; // Number of retry attempts
    private static final long RETRY_INTERVAL_MS = 5000; // Interval between retries (in milliseconds)

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();

        String url = System.getProperty("jdbc.url");
        String username = System.getProperty("jdbc.username");
        String password = System.getProperty("jdbc.password");

        if (url == null || username == null || password == null) {
            throw new IllegalStateException("Database environment variables are missing");
        }
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    private static void waitForDatabaseReady(String url, String username, String password) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try (Connection connection = new HikariDataSource(createHikariConfig(url, username, password)).getConnection()) {
                // Connection successful, exit retry loop
                return;
            } catch (SQLException e) {
                System.err.println("Database connection failed, retrying in " + RETRY_INTERVAL_MS + " ms... (Attempt " + (attempt + 1) + "/" + MAX_RETRIES + ")");
                attempt++;
                try {
                    Thread.sleep(RETRY_INTERVAL_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry sleep interrupted", ie);
                }
            }
        }
        throw new RuntimeException("Unable to connect to the database after " + MAX_RETRIES + " attempts");
    }

    private static HikariConfig createHikariConfig(String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        return config;
    }
}