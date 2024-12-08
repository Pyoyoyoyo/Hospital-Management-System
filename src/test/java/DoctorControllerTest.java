import com.main.hospitalmanagementsys.controllers.DoctorController;
import com.main.hospitalmanagementsys.database.DatabaseConnector;
import com.main.hospitalmanagementsys.model.Appointment;
import com.main.hospitalmanagementsys.model.Patient;
import com.main.hospitalmanagementsys.model.PatientPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorControllerTest {

    private DoctorController doctorController;

    @Mock
    private ComboBox<String> mockTimePeriodComboBox;
    @Mock
    private Label mockAppointmentCountLabel;
    @Mock
    private Label mockNewPatientCountLabel;
    @Mock
    private TableView<Appointment> mockAppointmentsTableView;
    @Mock
    private TableColumn<Appointment, String> mockTimeColumn;
    @Mock
    private TableColumn<Appointment, String> mockPatientNameColumn;
    @Mock
    private TableColumn<Appointment, String> mockDoctorNameColumn;

    @Mock
    private DatabaseConnector mockDatabaseConnector;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorController = new DoctorController();

        doctorController.timePeriodComboBox = mockTimePeriodComboBox;
        doctorController.appointmentCountLabel = mockAppointmentCountLabel;
        doctorController.newPatientCountLabel = mockNewPatientCountLabel;
        doctorController.AppointmentsTableView = mockAppointmentsTableView;
        doctorController.timeColumn = mockTimeColumn;
        doctorController.patientNameColumn = mockPatientNameColumn;
        doctorController.doctorNameColumn = mockDoctorNameColumn;

        mockStatic(DatabaseConnector.class);
    }

    @Test
    void testInitialize_populatesComboBox() {
        doctorController.initialize();

        verify(mockTimePeriodComboBox.getItems(), times(1))
                .addAll("7 хоногоор", "14 хоногоор", "1 сараар");
    }

    @Test
    void testInitialize_updatesNewPatientCountLabel() {
        when(DatabaseConnector.getNewAppointmentsCountByOneDayRange()).thenReturn(10);

        doctorController.initialize();

        verify(mockNewPatientCountLabel, times(1)).setText("10");
    }

    @Test
    void testInitialize_populatesAppointmentsTableView() {
        ObservableList<Appointment> mockAppointments = FXCollections.observableArrayList();
        when(DatabaseConnector.getActiveAppointments()).thenReturn(mockAppointments);

        doctorController.initialize();

        verify(mockAppointmentsTableView, times(1)).setItems(mockAppointments);
    }

    @Test
    void testClaimPayment_showsAlert() {
        PatientPayment mockPatientPayment = mock(PatientPayment.class);
        when(mockPatientPayment.getPatientName()).thenReturn("Test Patient");
        when(mockPatientPayment.getPaymentStatus()).thenReturn("Unpaid");

        doctorController.claimPayment(mockPatientPayment);

        verify(mockPatientPayment, times(1)).getPatientName();
        verify(mockPatientPayment, times(1)).getPaymentStatus();
    }

    @Test
    void testUpdatePieChart_updatesChartData() {
        when(DatabaseConnector.getPaymentStatusCounts(anyString())).thenReturn(
                Map.of("Paid", 10, "Unpaid", 5, "Overdue", 2)
        );

        doctorController.updatePieChart();

    }

    @Test
    void testNavigateToAppointments_showsAppointmentPage() {
        doctorController.homepage = mock(AnchorPane.class);
        doctorController.appointmentpage = mock(AnchorPane.class);
        doctorController.patientsinfopage = mock(AnchorPane.class);
        doctorController.paymentpage = mock(AnchorPane.class);

        doctorController.navigateToAppointments();

        verify(doctorController.homepage, times(1)).setVisible(false);
        verify(doctorController.appointmentpage, times(1)).setVisible(true);
        verify(doctorController.patientsinfopage, times(1)).setVisible(false);
        verify(doctorController.paymentpage, times(1)).setVisible(false);
    }
}
