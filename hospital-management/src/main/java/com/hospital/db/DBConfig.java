package com.hospital.db;

/**
 * Central place for database connection settings.
 * Database credentials are defined here.
 */
public final class DBConfig {

    private DBConfig() {
        // Utility class
    }

    public static final String URL =
            "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=UTC";

    public static final String USER = "root";
    public static final String PASSWORD = "YOUR_PASSWORD_HERE"; // replace with actual password
}
