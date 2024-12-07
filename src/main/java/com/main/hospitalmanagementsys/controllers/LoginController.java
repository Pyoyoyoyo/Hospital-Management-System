package com.main.hospitalmanagementsys.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("admin123")) {
            loadDoctorsView();
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    private void loadDoctorsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/main/hospitalmanagementsys/ui/doctor-home-view.fxml"));
            AnchorPane doctorsView = loader.load();

            Scene doctorsScene = new Scene(doctorsView);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(doctorsScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/main/hospitalmanagementsys/ui/forgot-password-view.fxml"));
            AnchorPane forgotPasswordView = loader.load();

            Scene forgotPasswordScene = new Scene(forgotPasswordView);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(forgotPasswordScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
