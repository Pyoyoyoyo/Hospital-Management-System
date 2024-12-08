package com.main.hospitalmanagementsys.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    public TextField emailField;

    /**
     * @description password shinechleh button click event.
     * emailee oruulsan uyd login ruu butsna
     * @param event nuuts ugee shinechleh tovchnii action event.
     * @return void
     * @author Tsagaadai, Sodbileg
     */
    @FXML
    public void handleResetPassword(ActionEvent event) {
        String email = emailField.getText();

        if (isValidEmail(email)) {
            System.out.println("Password reset link sent to: " + email);
            // Navigate back to the login form
            loadLoginForm();
        } else {
            System.out.println("Invalid email. Please try again.");
        }
    }

    /**
     * @description email haygiin format mun esehiig shalgana.
     *
     * @param email email address mun esehiig shalgana.
     * @return format ni mun bol Truee utga butsaana, ugui bol False utga butsaana
     */
    public boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    /**
     * @description nuuts ug sergeeh button daragdah uyd login formiig achaallana.
     *
     * @return void
     * @throws IOException fxml file oldohgui uyd exception butsaana.
     */
    public void loadLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/main/hospitalmanagementsys/ui/login-view.fxml"));
            AnchorPane loginView = loader.load();

            Scene loginScene = new Scene(loginView);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
