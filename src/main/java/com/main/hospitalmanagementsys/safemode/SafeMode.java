package com.main.hospitalmanagementsys.safemode;

import java.sql.*;

public class SafeMode {

    private static final String DEFAULT_DB_URL = "jdbc:h2:mem:testdb"; // Local fallback database

    public static Connection getDatabaseConnection() throws SQLException {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/HospitalManagement", "postgres", "91209913");
        } catch (SQLException e) {
            System.out.println("Failed to connect to remote database. Using fallback.");
            return DriverManager.getConnection(DEFAULT_DB_URL);
        }
    }
    public static String getUserData(int userId) {
        try (Connection conn = getDatabaseConnection()) {
            // Assume query user data from DB
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id = " + userId);
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Error accessing user data, using default value.");
        }
        return "Default User";
    }
}
