package com.main.hospitalmanagementsys.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private AnchorPane appointmentpage;

    @FXML
    private AnchorPane patientsinfopage;


    @FXML
    public void initialize() {
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
