import com.main.hospitalmanagementsys.database.DatabaseConnector;
import com.main.hospitalmanagementsys.model.Appointment;
import com.main.hospitalmanagementsys.model.AppointmentRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

class DatabaseConnectorTest {

    private DatabaseConnector databaseConnector;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        databaseConnector = mock(DatabaseConnector.class);
    }

    @Test
    void testGetActiveAppointmentsCountByDateRange() throws SQLException {
        // Mock the database connection and result set
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);  // Mock statement preparation
        when(preparedStatement.executeQuery()).thenReturn(resultSet);  // Mock query execution
        when(resultSet.next()).thenReturn(true);  // Simulate result set iteration
        when(resultSet.getInt(1)).thenReturn(5);  // Simulate returning a count of 5

        // Test for "7 хоногоор"
        int count = databaseConnector.getActiveAppointmentsCountByDateRange("7 хоногоор");
        assertEquals(0, count, "The active appointment count should be 5.");

        // Test for "14 хоногоор"
        count = databaseConnector.getActiveAppointmentsCountByDateRange("14 хоногоор");
        assertEquals(4, count, "The active appointment count should be 5.");
    }

    @Test
    void testGetNewAppointmentsCountByOneDayRange() throws SQLException {
        // Create mocks for the database connection, prepared statement, and result set
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        DatabaseConnector databaseConnector = mock(DatabaseConnector.class);  // Mock the databaseConnector

        // Define behavior for mocked methods
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);  // Mock statement preparation
        when(preparedStatement.executeQuery()).thenReturn(resultSet);  // Mock query execution
        when(resultSet.next()).thenReturn(true);  // Simulate result set iteration
        when(resultSet.getInt(1)).thenReturn(0);  // Simulate returning a count of 0

        // Test the method
        int count = databaseConnector.getNewAppointmentsCountByOneDayRange();
        assertEquals(1, count, "The new appointment count should be 1.");
    }



    @Test
    void testGetActiveAppointments() throws SQLException {
        // Mock the database connection, statement, and result set
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);  // Mock statement creation
        when(statement.executeQuery(any(String.class))).thenReturn(resultSet);  // Mock query execution
        when(resultSet.next()).thenReturn(true).thenReturn(false);  // Mock result set iteration
        when(resultSet.getString("time")).thenReturn("10:00");
        when(resultSet.getTimestamp("date")).thenReturn(Timestamp.valueOf("2024-12-18 10:00:00"));
        when(resultSet.getString("patient_name")).thenReturn("Jane Smith");
        when(resultSet.getString("doctor_name")).thenReturn("John Doe");

        // Call the method under test
        List<Appointment> appointments = databaseConnector.getActiveAppointments();

        // Assertions
        assertNotNull(appointments, "Appointments should not be null.");
        assertEquals(4, appointments.size(), "There should be 4 active appointment.");
    }

    @Test
    void testGetDoctorIdByName() {
        String doctorName = "John Doe";
        int doctorId = DatabaseConnector.getDoctorIdByName(doctorName);
        assertTrue(doctorId > 0, "Doctor ID should be a positive number.");
    }

    @Test
    void testGetPatientIdByName() {
        String patientName = "Jane Smith";
        int patientId = DatabaseConnector.getPatientIdByName(patientName);
        assertTrue(patientId > 0, "Patient ID should be a positive number.");
    }

    @Test
    void testGetPatientNameById() {
        int patientId = 1; // Use a valid patient ID here
        String patientName = DatabaseConnector.getPatientNameById(patientId);
        assertNotNull(patientName, "Patient name should not be null.");
    }

    @Test
    void testGetDoctorNameById() {
        int doctorId = 1; // Use a valid doctor ID here
        String doctorName = DatabaseConnector.getDoctorNameById(doctorId);
        assertNotNull(doctorName, "Doctor name should not be null.");
    }

    @Test
    void testAddNewAppointment() {
        AppointmentRecord appointment = new AppointmentRecord(
                505,
                1011,
                LocalDate.of(2024, 12, 18),
                "10:00",
                "active",
                1,  // doctorId
                1   // patientId
        );
        boolean isAdded = DatabaseConnector.addNewAppointment(appointment);
        assertTrue(isAdded, "Appointment should be successfully added.");
    }

    @Test
    void testUpdateAppointment() {
        AppointmentRecord appointment = new AppointmentRecord(
                505,
                1011,
                LocalDate.of(2024, 12, 18),
                "11:00",
                "Rescheduled",
                1,
                1
        );
        boolean isUpdated = DatabaseConnector.updateAppointment(appointment);
        assertTrue(isUpdated, "Appointment should be successfully updated.");
    }

    @Test
    void testDeleteAppointment() {
        AppointmentRecord appointment = new AppointmentRecord(
                505,
                1011,
                LocalDate.of(2024, 12, 18),
                "11:00",
                "Rescheduled",
                1,
                1
        );
        boolean isDeleted = DatabaseConnector.deleteAppointment(appointment);
        assertTrue(isDeleted, "Appointment should be successfully deleted.");
    }

    @Test
    void testConnect() {
        assertNotNull(DatabaseConnector.connect(), "Database connection should not be null.");
    }

    @Test
    void testGetDoctorIdByNameNotFound() {
        String doctorName = "NonExistent Doctor";
        assertThrows(IllegalArgumentException.class, () -> {
            DatabaseConnector.getDoctorIdByName(doctorName);
        }, "Should throw IllegalArgumentException when doctor is not found.");
    }

    @Test
    void testGetPatientIdByNameNotFound() {
        String patientName = "NonExistent Patient";
        assertThrows(IllegalArgumentException.class, () -> {
            DatabaseConnector.getPatientIdByName(patientName);
        }, "Should throw IllegalArgumentException when patient is not found.");
    }
}
