package com.hospital.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides JDBC database connections.
 * All database access goes through this class.
 */
public final class DBConnection {

    private DBConnection() {
        // Utility class
    }

    /**
     * Creates and returns a new database connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DBConfig.URL,
                DBConfig.USER,
                DBConfig.PASSWORD
        );
    }
}
