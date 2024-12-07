package com.main.hospitalmanagementsys.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private void handleResetPassword(ActionEvent event) {
        String email = emailField.getText();

        if (isValidEmail(email)) {
            System.out.println("Password reset link sent to: " + email);
        } else {
            System.out.println("Invalid email. Please try again.");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}
