package com.main.hospitalmanagementsys.controllers;

import com.main.hospitalmanagementsys.model.Appointment;
import com.main.hospitalmanagementsys.model.Patient;
import com.main.hospitalmanagementsys.model.PatientPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.main.hospitalmanagementsys.database.DatabaseConnector;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class DoctorController {

    @FXML
    private Button homeButton;

    @FXML
    private Button clientsButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button doctorsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private AnchorPane homepage;

    @FXML
    private Button addpatientbutton;

    @FXML
    private AnchorPane doctorspage;

    @FXML
    private AnchorPane appointmentpage;

    @FXML
    private AnchorPane patientsinfopage;
    @FXML
    private Label appointmentCountLabel;

    @FXML
    private Label newPatientCountLabel;

    @FXML
    private ComboBox<String> timePeriodComboBox;
    @FXML
    private TableView<Appointment> AppointmentsTableView;

    @FXML
    private TableColumn<Appointment, String> timeColumn;

    @FXML
    private TableColumn<Appointment, LocalDate> dateColumn;

    @FXML
    private TableColumn<Appointment, String> patientNameColumn;

    @FXML
    private TableColumn<Appointment, String> doctorNameColumn;

    @FXML
    private PieChart pieChart;

    @FXML
    private ComboBox<String> timeRangeComboBox;

    @FXML
    private TableView<PatientPayment> tableView;

    @FXML
    private TableColumn<PatientPayment, String> patientNameColumnonPayment;

    @FXML
    private TableColumn<PatientPayment, String> claimPaymentColumn;

    @FXML
    private TableView<Patient> patientTableView;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, String> medicalHistoryColumn;

    @FXML
    private TableColumn<Patient, String> insuranceInfoColumn;

    @FXML
    private TableColumn<Patient, String> registrationNumberColumn;

    @FXML
    private TableColumn<Patient, String> contactNumberColumn;

    @FXML
    private TableColumn<Patient, String> addressColumn;

    @FXML
    private TableColumn<Patient, String> emailColumn;

    @FXML
    private TableColumn<Patient, Void> editColumn;

    @FXML
    private TextField searchFieldOnPatient;

    private ObservableList<Patient> patientList;

    @FXML
    public void initialize() {
        timePeriodComboBox.getItems().addAll("7 хоногоор", "14 хоногоор", "1 сараар");
        timePeriodComboBox.setOnAction(event -> {
            String selectedValue = timePeriodComboBox.getSelectionModel().getSelectedItem();
            if (selectedValue != null) {
                int count = DatabaseConnector.getActiveAppointmentsCountByDateRange(selectedValue);
                appointmentCountLabel.setText(String.valueOf(count));
            }
        });

        int count = DatabaseConnector.getNewAppointmentsCountByOneDayRange();
        newPatientCountLabel.setText(String.valueOf(count));

        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        patientNameColumn.setCellValueFactory(cellData -> cellData.getValue().patientNameProperty());
        doctorNameColumn.setCellValueFactory(cellData -> cellData.getValue().doctorNameProperty());

        List<Appointment> appointments = DatabaseConnector.getActiveAppointments();
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
        AppointmentsTableView.setItems(appointmentList);

        timeRangeComboBox.getSelectionModel().select("7 хоногоор");
        timeRangeComboBox.setOnAction(event -> updatePieChart());
        updatePieChart();
        patientList = FXCollections.observableArrayList(DatabaseConnector.getAllPatients());
        patientTableView.setItems(patientList);
        patientNameColumnonPayment.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        claimPaymentColumn.setCellFactory(param -> new TableCell<PatientPayment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Button claimButton = new Button("Claim Payment");

                    claimButton.setOnAction(event -> {
                        PatientPayment patient = getTableView().getItems().get(getIndex());
                        claimPayment(patient);
                    });

                    setGraphic(claimButton);
                }
            }
        });

        populateTable();

        resetButtonStyles();
        setButtonSelected(homeButton);

        homeButton.setOnAction(event -> {
            onButtonClick(homeButton);
            navigateToHome();
        });

        clientsButton.setOnAction(event -> {
            onButtonClick(clientsButton);
            navigateToClients();
        });

        appointmentsButton.setOnAction(event -> {
            onButtonClick(appointmentsButton);
            navigateToAppointments();
        });

        doctorsButton.setOnAction(event -> {
            onButtonClick(doctorsButton);
            navigateToDoctors();
        });

        logoutButton.setOnAction(event -> {
            onButtonClick(logoutButton);
            logout();
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        medicalHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));
        insuranceInfoColumn.setCellValueFactory(new PropertyValueFactory<>("insuranceInformation"));
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        contactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        editColumn.setCellFactory(param -> new TableCell<Patient, Void>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    Patient patient = getTableRow().getItem();
                    if (patient != null) {
                        handleEditButton(patient);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });
        addpatientbutton.setOnAction(event -> handleAddPatientButton());
        loadPatientData();
        setupSearch();
    }

    private void resetButtonStyles() {
        resetButtonStyle(homeButton);
        resetButtonStyle(clientsButton);
        resetButtonStyle(appointmentsButton);
        resetButtonStyle(doctorsButton);
        resetButtonStyle(logoutButton);
    }

    private void resetButtonStyle(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8f98;");
    }

    private void setButtonSelected(Button button) {
        button.setStyle("-fx-background-color: rgba(76, 175, 80, 0.1); -fx-text-fill: #4caf50;");
    }

    private void onButtonClick(Button clickedButton) {
        resetButtonStyles();
        setButtonSelected(clickedButton);
    }
    private void navigateToHome() {
        try {
            doctorspage.setVisible(false);
            patientsinfopage.setVisible(false);
            homepage.setVisible(true);
            appointmentpage.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void navigateToClients() {
        System.out.println("Navigating to Clients");

        try {
            doctorspage.setVisible(false);
            patientsinfopage.setVisible(true);
            homepage.setVisible(false);
            appointmentpage.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToAppointments() {
        try {
            doctorspage.setVisible(false);
            patientsinfopage.setVisible(false);
            homepage.setVisible(false);
            appointmentpage.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToDoctors() {
        try {
            doctorspage.setVisible(true);
            patientsinfopage.setVisible(false);
            homepage.setVisible(false);
            appointmentpage.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePieChart() {
        String selectedRange = timeRangeComboBox.getSelectionModel().getSelectedItem();

        Map<String, Integer> paymentCounts = DatabaseConnector.getPaymentStatusCounts(selectedRange);

        PieChart.Data paidSlice = new PieChart.Data("Төлсөн", paymentCounts.getOrDefault("Paid", 0));
        PieChart.Data unpaidSlice = new PieChart.Data("Төлөөгүй", paymentCounts.getOrDefault("Unpaid", 0));
        PieChart.Data overdue = new PieChart.Data("Хугацаа хэтэрсэн", paymentCounts.getOrDefault("Overdue", 0));


        pieChart.getData().clear();
        pieChart.getData().addAll(paidSlice, unpaidSlice, overdue);
    }

    private void populateTable() {
        List<PatientPayment> patientPayments = DatabaseConnector.getPatientPaymentsFromDatabase();

        ObservableList<PatientPayment> data = FXCollections.observableArrayList(patientPayments);
        tableView.setItems(data);
    }


    private void claimPayment(PatientPayment patient) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Нэхэмжлэл Илгээх");
        alert.setHeaderText("Нэхэмжлэл илгээгдлээ");
        alert.setContentText("Үйлчлүүлэгч: " + patient.getPatientName() + "\nТөлбөрийн байдал: " + patient.getPaymentStatus());

        alert.showAndWait();
    }

    private void setupSearch() {
        FilteredList<Patient> filteredList = new FilteredList<>(patientList, p -> true);

        searchFieldOnPatient.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Text entered: " + newValue);

            filteredList.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();
                String patientName = patient.getPatientName().toLowerCase().trim();

                boolean matches = patientName.contains(lowerCaseFilter);
                System.out.println("Patient matches filter: " + matches);  // Debug line

                return matches;
            });
        });

        patientTableView.setItems(filteredList);
    }


    private void loadPatientData() {
        patientList = FXCollections.observableArrayList(DatabaseConnector.getAllPatients());

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getPatientNameProperty());

        patientTableView.setItems(patientList);
    }


    private void handleEditButton(Patient patient) {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Өвчтний мэдээлэл засварлах");

        ButtonType okButtonType = new ButtonType("Хадгалах", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Цуцлах", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        TextField nameField = new TextField(patient.getPatientName());
        TextField contactField = new TextField(patient.getContactNumber());
        TextField addressField = new TextField(patient.getAddress());
        TextField emailField = new TextField(patient.getEmail());

        grid.add(new Text("Өвчтний нэр:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Text("Утасны дугаар:"), 0, 1);
        grid.add(contactField, 1, 1);
        grid.add(new Text("Хаяг:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Text("Email:"), 0, 3);
        grid.add(emailField, 1, 3);

        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(contactField, Priority.ALWAYS);
        GridPane.setHgrow(addressField, Priority.ALWAYS);
        GridPane.setHgrow(emailField, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                patient.setPatientName(nameField.getText());
                patient.setContactNumber(contactField.getText());
                patient.setAddress(addressField.getText());
                patient.setEmail(emailField.getText());

                return patient;
            }
            return null;
        });

        Optional<Patient> result = dialog.showAndWait();
        result.ifPresent(updatedPatient -> {
            boolean success = DatabaseConnector.updatePatient(updatedPatient);
            if (success) {
                loadPatientData();
            } else {
                showErrorDialog("Update failed", "There was an error while updating the patient details.");
            }
        });
    }

    private void handleAddPatientButton() {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle("Шинэ өвчтөн нэмэх");

        ButtonType okButtonType = new ButtonType("Хадгалах", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Цуцлах", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        TextField nameField = new TextField();
        TextField contactField = new TextField();
        TextField addressField = new TextField();
        TextField emailField = new TextField();
        TextField medicalHistoryField = new TextField();
        TextField insuranceField = new TextField();
        TextField registrationNumberField = new TextField();

        grid.add(new Text("Өвчтний нэр:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Text("Утасны дугаар:"), 0, 1);
        grid.add(contactField, 1, 1);
        grid.add(new Text("Хаяг:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Text("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Text("Эмчилгээний түүх:"), 0, 4);
        grid.add(medicalHistoryField, 1, 4);
        grid.add(new Text("Даатгалын мэдээлэл:"), 0, 5);
        grid.add(insuranceField, 1, 5);
        grid.add(new Text("Регистрийн дугаар:"), 0, 6);
        grid.add(registrationNumberField, 1, 6);

        // Make sure fields expand when resized
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(contactField, Priority.ALWAYS);
        GridPane.setHgrow(addressField, Priority.ALWAYS);
        GridPane.setHgrow(emailField, Priority.ALWAYS);
        GridPane.setHgrow(medicalHistoryField, Priority.ALWAYS);
        GridPane.setHgrow(insuranceField, Priority.ALWAYS);
        GridPane.setHgrow(registrationNumberField, Priority.ALWAYS);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Patient newPatient = new Patient(
                        nameField.getText(),
                        medicalHistoryField.getText(),
                        insuranceField.getText(),
                        registrationNumberField.getText(),
                        contactField.getText(),
                        addressField.getText(),
                        emailField.getText()
                );
                return newPatient;
            }
            return null;
        });

        Optional<Patient> result = dialog.showAndWait();

        result.ifPresent(newPatient -> {
            boolean success = DatabaseConnector.addNewPatient(newPatient);
            if (success) {
                loadPatientData();
            } else {
                showErrorDialog("Adding failed", "There was an error while adding the new patient.");
            }
        });
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void logout() {
        try {
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/main/hospitalmanagementsys/ui/login-view.fxml"));
            AnchorPane loginView = loader.load();
            Scene loginScene = new Scene(loginView);
            Stage stage = new Stage();
            stage.setScene(loginScene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
