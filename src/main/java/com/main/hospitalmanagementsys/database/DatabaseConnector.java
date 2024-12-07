package com.main.hospitalmanagementsys.database;

import com.main.hospitalmanagementsys.model.*;

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
    /* data base configuration */
    private static final String URL = "jdbc:postgresql://localhost:5432/HospitalManagement"; //db name
    private static final String USER = "postgres";  //username
    private static final String PASSWORD = "91209913";  //password
    /**
     * @description Database iin holboltiig guitsetgeh bolon database iin statusiig iltgeh zorilgotoi funtion.
     *
     * @return Connection
     *
     * @author Tsagaadai, Sodbileg
     */
    public static Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL database!");
        } catch (SQLException e) {
            System.out.println("Connection failed due to SQLException.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            e.printStackTrace();
        }

        if (connection == null) {
            System.out.println("Connection object is null.");
        }
        return connection;
    }
    /**
     * @description Idevhtei buyu status n active baih uyiin uulzaltiin medeelliig database-ees ugugdul awchrah function.
     *
     * @return int
     *
     * @author Tsagaadai, Sodbileg
     */
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
    /**
     * @description Shine uulzaltiin tsag buyu timerange
     * n 1 odriin dotor baih uulzaltiig database-ees shuuj awah function.
     *
     * @return int
     *
     * @author Tsagaadai, Sodbileg
     */
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
    /**
     * @description status n active baih uulzaltuudiin ugugdliig database-ees awah function.
     *
     * @return List<Appointment></>
     *
     * @author Tsagaadai, Sodbileg
     */
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
    /**
     * @description Database-ees statusiin utgaar shuult hiij uulzaltuudiin medeelel awah function.
     *
     * @return List<AppointmentRecord>
     *
     * @author Tsagaadai, Sodbileg
     */
    public static List<AppointmentRecord> getAppointmentRecordsByStatus(String status) {
        List<AppointmentRecord> appointmentRecords = new ArrayList<>();
        String query = "SELECT ar.appointment_code, ar.time, ar.date, ar.patient_id, ar.doctor_id, ar.status, " +
                "p1.name AS patient_name, p2.name AS doctor_name " +
                "FROM appointment_record ar " +
                "JOIN patient p ON ar.patient_id = p.id " +
                "JOIN doctor d ON ar.doctor_id = d.id " +
                "JOIN person p1 ON p.person_id = p1.id " +
                "JOIN person p2 ON d.person_id = p2.id " +
                "WHERE ar.status = ?"; // Use parameterized query for status

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the status parameter (either 'active' or 'inactive')
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int appointmentCode = rs.getInt("appointment_code");
                    String time = rs.getString("time");
                    LocalDate date = null;

                    try {
                        date = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
                    } catch (Exception e) {
                        System.out.println("Error converting date: " + e.getMessage());
                    }

                    int patientId = rs.getInt("patient_id");
                    int doctorId = rs.getInt("doctor_id");
                    String statusFromDb = rs.getString("status");

                    // Fetch patient name and doctor name
                    String patientName = rs.getString("patient_name");
                    String doctorName = rs.getString("doctor_name");

                    if (patientName != null && doctorName != null && statusFromDb != null) {
                        // Create the AppointmentRecord with the fetched names
                        AppointmentRecord appointmentRecord = new AppointmentRecord(
                                0, appointmentCode, date, time, statusFromDb, doctorId, patientId);

                        appointmentRecord.setPatientName(patientName);
                        appointmentRecord.setDoctorName(doctorName);

                        appointmentRecords.add(appointmentRecord);
                    } else {
                        System.out.println("Skipping appointment due to missing data.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }

        return appointmentRecords;
    }

    /**
     * @description Tulburiin medeelliig timerange teigeer filterden awah function.
     *
     * @return Map<String, Integer>
     *
     * @author Tsagaadai, Sodbileg
     */
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

    /**
     * @description Hereglegchiin tulburiin medeelliig database-ees awah uildel guitsetgeh function.
     *
     * @return List<PatientPayment>
     *
     * @author Tsagaadai, Sodbileg
     */
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
    /**
     * @description Database-ees buh hereglegchiin medeelliig list-eer awah function.
     *
     * @return List<Patient>
     *
     * @author Tsagaadai, Sodbileg
     */
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
    /**
     * @description Database-d shine hereglegch nemeh insertion query guitsetgeh function.
     *
     * @return boolean
     *
     * @author Tsagaadai, Sodbileg
     */
    public static boolean addNewPatient(Patient patient) {
        String insertPersonQuery = "INSERT INTO person (name, contact_number, address, email, registration_number) VALUES (?, ?, ?, ?, ?)";
        String insertPatientQuery = "INSERT INTO patient (medical_history, insurance_information, person_id) VALUES (?, ?, ?)";

        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

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
                                conn.commit();
                                return patientRowsAffected > 0;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * @description Database-d hereglegchiin medeelel shinechleh Update query guitsetgeh function.
     *
     * @return boolean
     *
     * @author Tsagaadai, Sodbileg
     */
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
    /**
     * @description Database-d hereglegch ustgah delete query guitsetgeh function.
     *
     * @return boolean
     *
     * @author Tsagaadai, Sodbileg
     */
    public static boolean deletePatient(Patient patient) {
        String deletePatientQuery = "DELETE FROM patient WHERE person_id = (SELECT id FROM person WHERE registration_number = ?)";
        String deletePersonQuery = "DELETE FROM person WHERE registration_number = ?";

        try (Connection conn = connect();
             PreparedStatement deletePatientStmt = conn.prepareStatement(deletePatientQuery);
             PreparedStatement deletePersonStmt = conn.prepareStatement(deletePersonQuery)) {

            deletePatientStmt.setString(1, patient.getRegistrationNumber());
            int patientRowsAffected = deletePatientStmt.executeUpdate();

            deletePersonStmt.setString(1, patient.getRegistrationNumber());
            int personRowsAffected = deletePersonStmt.executeUpdate();

            return patientRowsAffected > 0 && personRowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    /**
     * @description Uulzaltiin code shineer uusgeh function.
     *
     * @return int
     *
     * @author Tsagaadai, Sodbileg
     */
    public static int generateAppointmentCode() {
        String query = "SELECT MAX(appointment_code) AS max_code FROM appointment_record";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int maxCode = rs.getInt("max_code");
                return maxCode + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1000;
    }
    /**
     * @description Database-ees doctoriin idg doctoriin nernees hargalzuulan awah function.
     *
     * @return int
     *
     * @author Tsagaadai, Sodbileg
     */
    public static int getDoctorIdByName(String doctorName) {
        String query = "SELECT d.id FROM doctor d JOIN person p ON d.person_id = p.id WHERE p.name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, doctorName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Doctor not found: " + doctorName);
    }
    /**
     * @description Database-d hereglegchiin id-g  nereer hargalzuulan awah function.
     *
     * @return int
     *
     * @author Tsagaadai, Sodbileg
     */
    public static int getPatientIdByName(String patientName) {
        String query = "SELECT p.id FROM patient p JOIN person pe ON p.person_id = pe.id WHERE pe.name = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, patientName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Patient not found: " + patientName);
    }
    /**
     * @description Database-ees hereglegchiin neriig hereglegchiin
     * ID tai hargalzuulan awah function.
     *
     * @return String
     *
     * @author Tsagaadai, Sodbileg
     */
    public static String getPatientNameById(int patientId) {
        String query = "SELECT p1.name " +
                "FROM person p1 " +
                "JOIN patient p ON p1.id = p.person_id " +
                "WHERE p.id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error while fetching patient name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @description .
     *
     * @return String
     *
     * @author Tsagaadai, Sodbileg
     */
    public static String getDoctorNameById(int doctorId) {
        String query = "SELECT p2.name " +
                "FROM person p2 " +
                "JOIN doctor d ON p2.id = d.person_id " +
                "WHERE d.id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error while fetching doctor name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description Database-d uulzalt nemeh buyu insertion query guitsetgeh function.
     *
     * @return boolean
     *
     * @author Tsagaadai, Sodbileg
     */
    public static boolean addNewAppointment(AppointmentRecord appointment) {
        String query = "INSERT INTO appointment_record (appointment_code, date, time, status, doctor_id, patient_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, appointment.getAppointmentCode());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getDate().atStartOfDay()));
            stmt.setString(3, appointment.getTime());
            stmt.setString(4, appointment.getStatus());
            stmt.setInt(5, appointment.getDoctorId());
            stmt.setInt(6, appointment.getPatientId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * @description Database-d uulzalt zasah, shinechleh buyu update query guitsetgeh function.
     *
     * @return boolean
     *
     * @author Tsagaadai, Sodbileg
     */
    public static boolean updateAppointment(AppointmentRecord appointment) {
        String updateAppointmentQuery = "UPDATE appointment_record SET date = ?, time = ?, status = ? WHERE appointment_code = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(updateAppointmentQuery)) {

            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getDate().atStartOfDay())); // Assuming LocalDate for date
            stmt.setString(2, appointment.getTime());
            stmt.setString(3, appointment.getStatus());
            stmt.setInt(4, appointment.getAppointmentCode()); // Added missing binding for appointment_code

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * @description Database-d uulzalt ustgah buyu delete query guitsetgeh function.
     *
     * @return boolean
     *
     * @author Tsagaadai, Sodbileg
     */
    public static boolean deleteAppointment(AppointmentRecord appointment) {
        String deleteAppointmentQuery = "DELETE FROM appointment_record WHERE appointment_code = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(deleteAppointmentQuery)) {

            stmt.setInt(1, appointment.getAppointmentCode()); // Added missing binding for appointment_code

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
