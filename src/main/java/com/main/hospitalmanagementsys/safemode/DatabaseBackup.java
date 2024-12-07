package com.main.hospitalmanagementsys.safemode;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatabaseBackup {

    public static void backupDatabase() {
        String backupCommand = "BACKUP DATABASE HospitalManagement TO DISK = 'C:\\backup_location.bak'";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/HospitalManagement", "postgres", "91209913");
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(backupCommand);
            System.out.println("Database backed up successfully.");
        } catch (SQLException e) {
            System.out.println("Error during database backup: " + e.getMessage());
        }
    }

    public static void scheduleBackup() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> backupDatabase(), 0, 30, TimeUnit.MINUTES);
    }
}
