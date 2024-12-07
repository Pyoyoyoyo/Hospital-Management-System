package com.main.hospitalmanagementsys.controllers;

import com.main.hospitalmanagementsys.model.*;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.main.hospitalmanagementsys.database.DatabaseConnector;


import java.time.LocalDate;
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
    private Button logoutButton;
    @FXML
    private AnchorPane homepage;

    @FXML
    private Button addpatientbutton;

    @FXML
    private Button addappointmentbutton;

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
    private TableColumn<PatientPayment, String> patientNameColumnOnPayment;

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
    @FXML
    public TextField searchFieldOnAppointment;
    @FXML
    public  TextField searchFieldByDateOnAppointment;

    @FXML
    private Button newAppointmentsButton;

    @FXML
    private Button completedAppointmentsButton;
    @FXML
    public  DatePicker datePickerByDateOnAppointment;

    private ObservableList<Patient> patientList;
    private ObservableList<AppointmentRecord> appointmentRecordsList;

    @FXML
    private TableView<AppointmentRecord> AppointmentRecordsTableView;
    @FXML
    private TableColumn<AppointmentRecord, String> appointmentTimeColumn;
    @FXML
    private TableColumn<AppointmentRecord, String> appointmentDateColumn;
    @FXML
    private TableColumn<AppointmentRecord, String> appointmentPatientNameColumn;
    @FXML
    private TableColumn<AppointmentRecord, String> appointmentDoctorNameColumn;
    @FXML
    private TableColumn<AppointmentRecord, String> appointmentStatusColumn;
    @FXML
    private TableColumn<AppointmentRecord, Void> appointmentEditColumn;
    @FXML
    private FilteredList<AppointmentRecord> filteredAppointmentList;


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

        timeRangeComboBox.getSelectionModel().select("7 хоногоор");
        timeRangeComboBox.setOnAction(event -> updatePieChart());
        updatePieChart();
        patientList = FXCollections.observableArrayList(DatabaseConnector.getAllPatients());
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList(appointments);
        appointmentRecordsList = FXCollections.observableArrayList(DatabaseConnector.getAppointmentRecordsByStatus("active"));
        AppointmentsTableView.setItems(appointmentList);
        patientTableView.setItems(patientList);
        AppointmentRecordsTableView.setItems(appointmentRecordsList);
        patientNameColumnOnPayment.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        claimPaymentColumn.setCellFactory(param -> new TableCell<PatientPayment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Button claimButton = new Button("Төлбөр Нэхэмжлэх");
                    claimButton.setStyle("-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white;");

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

        resetButtonStylesOnAppointments();
        setButtonSelectedOnAppointments(newAppointmentsButton);
        newAppointmentsButton.setOnAction(e -> {
            onButtonClickOnAppointments(newAppointmentsButton);
            handleNewAppointments();
        });
        completedAppointmentsButton.setOnAction(e -> {
            onButtonClickOnAppointments(completedAppointmentsButton);
            handleCompletedAppointments();
        });
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
            private final Button editButton = new Button("Засах");
            private final Button deleteButton = new Button("Устгах");
            {
                editButton.setOnAction(event -> {
                    Patient patient = getTableRow().getItem();
                    if (patient != null) {
                        handleEditButton(patient);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Patient patient = getTableRow().getItem();
                    if (patient != null) {
                        handleDeleteButton(patient);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(10, editButton, deleteButton);
                    editButton.setStyle("-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white;");
                    setGraphic(buttonBox);
                }
            }

        });

        addpatientbutton.setOnAction(event -> handleAddPatientButton());
        addappointmentbutton.setOnAction(event -> handleAddAppointmentButton());
        loadPatientData();
        searchPatient();

        appointmentTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        appointmentDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        appointmentPatientNameColumn.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientId();
            String patientName = DatabaseConnector.getPatientNameById(patientId);
            return new SimpleStringProperty(patientName);
        });
        appointmentDoctorNameColumn.setCellValueFactory(cellData -> {
            int doctorId = cellData.getValue().getDoctorId();
            String doctorName = DatabaseConnector.getDoctorNameById(doctorId);
            return new SimpleStringProperty(doctorName);
        });
        appointmentStatusColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().getStatus())));

        handleNewAppointments();

        searchAppointment();
        searchAppointmentByDate();
        appointmentEditColumn.setCellFactory(param -> new TableCell<AppointmentRecord, Void>() {
            private final Button editButton = new Button("Засах");
            private final Button deleteButton = new Button("Устгах");

            {
                editButton.setOnAction(event -> {
                    AppointmentRecord appointment = getTableRow().getItem();
                    if (appointment != null) {
                        handleEditAppointmentButton(appointment);
                    }
                });

                deleteButton.setOnAction(event -> {
                    AppointmentRecord appointment = getTableRow().getItem();
                    if (appointment != null) {
                        handleDeleteAppointmentButton(appointment);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonBox = new HBox(10, editButton, deleteButton);
                    editButton.setStyle("-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white;");
                    deleteButton.setStyle("-fx-background-color: #4CAF50; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: white;");
                    setGraphic(buttonBox);
                }
            }
        });

    }

    private void resetButtonStylesOnAppointments() {
        resetButtonStyleOnAppointments(newAppointmentsButton);
        resetButtonStyleOnAppointments(completedAppointmentsButton);
    }

    private void resetButtonStyleOnAppointments(Button button) {
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: GREY; -fx-border-width: 0 0 2 0; -fx-font-size: 14;");
    }
    private void setButtonSelectedOnAppointments(Button button) {
        button.setStyle("-fx-background-color: rgba(76, 175, 80, 0.1); -fx-text-fill: #4CAF50; -fx-border-width: 0 0 2 0; -fx-font-size: 14;");
    }

    private void onButtonClickOnAppointments(Button clickedButton) {
        resetButtonStylesOnAppointments();

        setButtonSelectedOnAppointments(clickedButton);
    }

    private void resetButtonStyles() {
        resetButtonStyle(homeButton);
        resetButtonStyle(clientsButton);
        resetButtonStyle(appointmentsButton);
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
            patientsinfopage.setVisible(true);
            homepage.setVisible(false);
            appointmentpage.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToAppointments() {
        try {
            patientsinfopage.setVisible(false);
            homepage.setVisible(false);
            appointmentpage.setVisible(true);
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

    private void searchPatient() {
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
                System.out.println("Patient matches filter: " + matches);

                return matches;
            });
        });

        patientTableView.setItems(filteredList);
    }

    private void searchAppointment() {
        filteredAppointmentList = new FilteredList<>(appointmentRecordsList, appointment -> true);

        searchFieldOnAppointment.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Text entered: " + newValue);

            filteredAppointmentList.setPredicate(appointment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase().trim();
                String patientName = appointment.getPatientName().toLowerCase().trim();

                System.out.println("Patient Name: " + patientName);

                boolean matches = patientName.contains(lowerCaseFilter);

                System.out.println("Appointment matches filter: " + matches);

                return matches;
            });
        });

        AppointmentRecordsTableView.setItems(filteredAppointmentList);
    }

    private void searchAppointmentByDate() {
        filteredAppointmentList = new FilteredList<>(appointmentRecordsList, appointment -> true);

        datePickerByDateOnAppointment.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected date: " + newValue);

            filteredAppointmentList.setPredicate(appointment -> {
                if (newValue == null) {
                    return true;
                }

                LocalDate appointmentDate = appointment.getDate();

                boolean matches = appointmentDate.equals(newValue);

                System.out.println("Appointment matches filter by date: " + matches);

                return matches;
            });

            AppointmentRecordsTableView.setItems(filteredAppointmentList);
        });
    }

    private void loadPatientData() {
        patientList = FXCollections.observableArrayList(DatabaseConnector.getAllPatients());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getPatientNameProperty());
        patientTableView.setItems(patientList);
    }

    @FXML
    private void handleNewAppointments() {
        List<AppointmentRecord> records = DatabaseConnector.getAppointmentRecordsByStatus("active");
        appointmentRecordsList.setAll(records);
        AppointmentRecordsTableView.setItems(appointmentRecordsList);
    }

    @FXML
    private void handleCompletedAppointments() {
        List<AppointmentRecord> inactiveRecords = DatabaseConnector.getAppointmentRecordsByStatus("inactive");
        appointmentRecordsList.setAll(inactiveRecords);
        AppointmentRecordsTableView.setItems(appointmentRecordsList);
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


    private void handleDeleteButton(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Бататгах");
        alert.setHeaderText("Энэхүү өвчтөнийг устгахдаа итгэлтэй байна уу?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = DatabaseConnector.deletePatient(patient);
            if (success) {
                patientList.remove(patient);
            } else {
                showErrorDialog("Deletion failed", "There was an error while deleting the patient.");
            }
        }
    }

    private void handleAddAppointmentButton() {
        Dialog<AppointmentRecord> dialog = new Dialog<>();
        dialog.setTitle("Шинэ уулзалт нэмэх");

        ButtonType saveButtonType = new ButtonType("Хадгалах", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Цуцлах", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        TextField doctorNameField = new TextField();
        TextField patientNameField = new TextField();

        grid.add(new Text("Он сар:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Text("Цаг:"), 0, 1);
        grid.add(timeField, 1, 1);
        grid.add(new Text("Эмчийн нэр:"), 0, 2);
        grid.add(doctorNameField, 1, 2);
        grid.add(new Text("Өвчтөний нэр:"), 0, 3);
        grid.add(patientNameField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    if (datePicker.getValue() == null || timeField.getText().isEmpty() ||
                            doctorNameField.getText().isEmpty() || patientNameField.getText().isEmpty()) {
                        throw new IllegalArgumentException("Бүх талбарыг бөглөнө үү.");
                    }

                    int generatedCode = DatabaseConnector.generateAppointmentCode();
                    int doctorId = DatabaseConnector.getDoctorIdByName(doctorNameField.getText());
                    int patientId = DatabaseConnector.getPatientIdByName(patientNameField.getText());

                    return new AppointmentRecord(
                            0,
                            generatedCode,
                            datePicker.getValue(),
                            timeField.getText(),
                            "active",
                            doctorId,
                            patientId
                    );
                } catch (Exception e) {
                    showErrorDialog("Зөвшөөрөгдөөгүй утга", e.getMessage());
                }
            }
            return null;
        });

        Optional<AppointmentRecord> result = dialog.showAndWait();

        result.ifPresent(newAppointment -> {
            boolean success = DatabaseConnector.addNewAppointment(newAppointment);
            if (success) {
                handleNewAppointments();
            } else {
                showErrorDialog("Adding Appointment Failed", "There was an error while saving the new appointment.");
            }
        });
    }



    private void handleEditAppointmentButton(AppointmentRecord appointment) {
        Dialog<AppointmentRecord> dialog = new Dialog<>();
        dialog.setTitle("Уулзалтыг өөрчлөх");

        ButtonType okButtonType = new ButtonType("Хадгалах", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Цуцлах", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        TextField dateField = new TextField(appointment.getDate().toString());
        TextField timeField = new TextField(appointment.getTime());
        TextField statusField = new TextField(appointment.getStatus());

        grid.add(new Text("Он сар:"), 0, 0);
        grid.add(dateField, 1, 0);
        grid.add(new Text("Цаг:"), 0, 1);
        grid.add(timeField, 1, 1);
        grid.add(new Text("Төлөв:"), 0, 2);
        grid.add(statusField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                appointment.setDate(LocalDate.parse(dateField.getText()));
                appointment.setTime(timeField.getText());
                appointment.setStatus(statusField.getText());
                return appointment;
            }
            return null;
        });

        Optional<AppointmentRecord> result = dialog.showAndWait();
        result.ifPresent(updatedAppointment -> {
            boolean success = DatabaseConnector.updateAppointment(updatedAppointment);
            if (success) {
                handleNewAppointments();
            } else {
                showErrorDialog("Update failed", "There was an error while updating the appointment details.");
            }
        });
    }

    private void handleDeleteAppointmentButton(AppointmentRecord appointment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Уулзалтыг цуцлах");
        alert.setHeaderText("Уулзалтыг устгахдаа итгэлтэй байна уу?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = DatabaseConnector.deleteAppointment(appointment);
            if (success) {
                appointmentRecordsList.remove(appointment);
                handleNewAppointments();
            } else {
                showErrorDialog("Deletion failed", "There was an error while deleting the appointment.");
            }
        }
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
