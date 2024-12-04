package com.main.hospitalmanagementsys.controllers;

import com.main.hospitalmanagementsys.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.main.hospitalmanagementsys.database.DatabaseConnector;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    private AnchorPane doctorspage;

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
        AppointmentsTableView.setItems(appointmentList);;

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

    @FXML
    private void navigateToClients() {
        System.out.println("Navigating to Clients");

        try {
            doctorspage.setVisible(false);
            patientsinfopage.setVisible(true);
            homepage.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToHome() {
        try {
            doctorspage.setVisible(false);
            patientsinfopage.setVisible(false);
            homepage.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToAppointments() {
        System.out.println("Navigating to Appointments");
    }

    private void navigateToDoctors() {
        try {
            doctorspage.setVisible(true);
            patientsinfopage.setVisible(false);
            homepage.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
