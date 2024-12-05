package com.main.hospitalmanagementsys.database;

import com.main.hospitalmanagementsys.model.Appointment;
import com.main.hospitalmanagementsys.model.Patient;
import com.main.hospitalmanagementsys.model.PatientPayment;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "JOIN patient p ON ar.patient_id = p.id " +
                "JOIN doctor d ON ar.doctor_id = d.id " +
                "JOIN person p1 ON p.person_id = p1.id " +
                "JOIN person p2 ON d.person_id = p2.id " +
                "WHERE ar.status = 'active'";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String time = rs.getString("time");
                LocalDate date = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
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

    public static Map<String, Integer> getPaymentStatusCounts(String selectedValue) {
        String query = "SELECT status, COUNT(*) FROM payment WHERE status IN ('Paid', 'Unpaid', 'Overdue') AND date >= ? GROUP BY status";
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

        Map<String, Integer> statusCounts = new HashMap<>();
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (filterDate != null) {
                stmt.setDate(1, Date.valueOf(filterDate));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String status = rs.getString("status");
                    int count = rs.getInt(2);
                    statusCounts.put(status, count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        statusCounts.putIfAbsent("Paid", 0);
        statusCounts.putIfAbsent("Unpaid", 0);
        statusCounts.putIfAbsent("Overdue", 0);

        return statusCounts;
    }


    public static List<PatientPayment> getPatientPaymentsFromDatabase() {
        List<PatientPayment> payments = new ArrayList<>();

        String query = "SELECT p.name AS patient_name, pay.status AS payment_status " +
                "FROM payment pay " +
                "JOIN patient pat ON pay.patient_id = pat.id " +
                "JOIN person p ON pat.person_id = p.id " +
                "WHERE pay.status IN ('Unpaid', 'Overdue')";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String patientName = rs.getString("patient_name");
                String paymentStatus = rs.getString("payment_status");
                payments.add(new PatientPayment(patientName, paymentStatus));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payments;
    }

    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT p.name, p.contact_number, p.address, p.email, pt.medical_history, pt.insurance_information, p.registration_number " +
                "FROM person p " +
                "JOIN patient pt ON p.id = pt.person_id";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String contactNumber = rs.getString("contact_number");
                String address = rs.getString("address");
                String email = rs.getString("email");
                String medicalHistory = rs.getString("medical_history");
                String insuranceInformation = rs.getString("insurance_information");
                String registrationNumber = rs.getString("registration_number");

                // Assuming Patient constructor takes these fields
                Patient patient = new Patient(name, medicalHistory, insuranceInformation, registrationNumber,
                        contactNumber, address, email);
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public static boolean addNewPatient(Patient patient) {
        String insertPersonQuery = "INSERT INTO person (name, contact_number, address, email, registration_number) VALUES (?, ?, ?, ?, ?)";
        String insertPatientQuery = "INSERT INTO patient (medical_history, insurance_information, person_id) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            try (PreparedStatement insertPersonStmt = conn.prepareStatement(insertPersonQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertPersonStmt.setString(1, patient.getPatientName());
                insertPersonStmt.setString(2, patient.getContactNumber());
                insertPersonStmt.setString(3, patient.getAddress());
                insertPersonStmt.setString(4, patient.getEmail());
                insertPersonStmt.setString(5, patient.getRegistrationNumber());

                int affectedRows = insertPersonStmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = insertPersonStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int personId = generatedKeys.getInt(1);

                            try (PreparedStatement insertPatientStmt = conn.prepareStatement(insertPatientQuery)) {
                                insertPatientStmt.setString(1, patient.getMedicalHistory());
                                insertPatientStmt.setString(2, patient.getInsuranceInformation());
                                insertPatientStmt.setInt(3, personId);

                                int patientRowsAffected = insertPatientStmt.executeUpdate();
                                return patientRowsAffected > 0;
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean updatePatient(Patient patient) {
        String updatePersonQuery = "UPDATE person SET name = ?, contact_number = ?, address = ?, email = ? WHERE registration_number = ?";
        String updatePatientQuery = "UPDATE patient SET medical_history = ?, insurance_information = ? WHERE person_id = ?";

        try (Connection conn = connect();
             PreparedStatement updatePersonStmt = conn.prepareStatement(updatePersonQuery);
             PreparedStatement updatePatientStmt = conn.prepareStatement(updatePatientQuery)) {

            updatePersonStmt.setString(1, patient.getPatientName());
            updatePersonStmt.setString(2, patient.getContactNumber());
            updatePersonStmt.setString(3, patient.getAddress());
            updatePersonStmt.setString(4, patient.getEmail());

            updatePersonStmt.setString(5, patient.getRegistrationNumber());  // registration_number is now treated as a String

            int personRowsAffected = updatePersonStmt.executeUpdate();

            String getPersonIdQuery = "SELECT id FROM person WHERE registration_number = ?";
            try (PreparedStatement getPersonIdStmt = conn.prepareStatement(getPersonIdQuery)) {
                getPersonIdStmt.setString(1, patient.getRegistrationNumber());
                ResultSet rs = getPersonIdStmt.executeQuery();

                if (rs.next()) {
                    int personId = rs.getInt("id");

                    updatePatientStmt.setString(1, patient.getMedicalHistory());
                    updatePatientStmt.setString(2, patient.getInsuranceInformation());

                    updatePatientStmt.setInt(3, personId);

                    int patientRowsAffected = updatePatientStmt.executeUpdate();

                    return personRowsAffected > 0 && patientRowsAffected > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    public static void main(String[] args) {
        Connection conn = connect();
        if (conn != null) {
        }
    }
}
