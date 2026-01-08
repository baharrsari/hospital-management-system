package com.hospital.util; // utility package for database helpers
import java.sql.Connection; // JDBC Connection interface

import com.hospital.db.DBConnection; // helper to obtain DB connections

/**
 * Simple database connection test class.
 * This class is used only to verify that MySQL JDBC connection works.
 */
public class DBTest {

    public static void main(String[] args) { // entry point to run the DB test
        System.out.println("Starting database connection test..."); // notify start

        try (Connection connection = DBConnection.getConnection()) { // acquire connection

            if (connection != null && !connection.isClosed()) { // check connection validity
                System.out.println("SUCCESS: Database connection established."); // success message
            } else {
                System.out.println("FAIL: Connection object is null or closed."); // failure message
            }

        } catch (Exception e) { // handle any exceptions during connect
            System.out.println("ERROR: Database connection failed."); // error notice
            e.printStackTrace(); // print stacktrace for debugging
        }
    }
}
