package com.main.hospitalmanagementsys.database;

import com.main.hospitalmanagementsys.model.Appointment;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    private static final String URL = "jdbc:postgresql://localhost:5432/HospitalManagement"; //db name
    private static final String USER = "postgres";  //username
    private static final String PASSWORD = "91209913";  //password

    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL database!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
        return connection;
    }

    public static int getActiveAppointmentsCountByDateRange(String selectedValue) {
        String query = "SELECT COUNT(*) FROM appointment_record WHERE status = 'active' AND date >= ?";
        LocalDate currentDate = LocalDate.now();
        LocalDate filterDate = null;

        switch (selectedValue) {
            case "7 хоногоор":
                filterDate = currentDate.minus(7, ChronoUnit.DAYS);
                break;
            case "14 хоногоор":
                filterDate = currentDate.minus(14, ChronoUnit.DAYS);
                break;
            case "1 сараар":
                filterDate = currentDate.minus(1, ChronoUnit.MONTHS);
                break;
        }

        int count = 0;
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (filterDate != null) {
                stmt.setDate(1, Date.valueOf(filterDate));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getNewAppointmentsCountByOneDayRange() {
        LocalDate currentDate = LocalDate.now();
        String query = "SELECT COUNT(*) FROM appointment_record WHERE status = 'active' AND date::date = ?";

        int count = 0;
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(currentDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<Appointment> getActiveAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT ar.time, ar.date, p1.name AS patient_name, p2.name AS doctor_name " +
                "FROM appointment_record ar " +
                "JOIN patient p ON ar.patient_id = p.id " +  // patient ID is in the patient table
                "JOIN doctor d ON ar.doctor_id = d.id " +   // doctor ID is in the doctor table
                "JOIN person p1 ON p.person_id = p1.id " +  // joining person table for patient
                "JOIN person p2 ON d.person_id = p2.id " +  // joining person table for doctor
                "WHERE ar.status = 'active'";  // filter for active appointments

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String time = rs.getString("time");
                LocalDate date = rs.getTimestamp("date").toLocalDateTime().toLocalDate(); // Timestamp to LocalDate
                String patientName = rs.getString("patient_name");
                String doctorName = rs.getString("doctor_name");

                Appointment appointment = new Appointment(time, date, patientName, doctorName);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }


    public static void main(String[] args) {
        Connection conn = connect();
        if (conn != null) {
        }
    }
}
