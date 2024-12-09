package com.main.hospitalmanagementsys.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    private static final String DOCTOR_HOME_VIEW = "/com/main/hospitalmanagementsys/ui/doctor-home-view.fxml";
    private static final String FORGOT_PASSWORD_VIEW = "/com/main/hospitalmanagementsys/ui/forgot-password-view.fxml";

    /**
     * @description Login button buyu login hiigdeh uyd buyu shiljih uildliin function.
     * herew doctoriin newtreh ner password zuv bol doctoriin module ruu shiljine.
     *
     * @param event login button daragdah uyd enehuu action hiigdene.
     * @return void
     * @author Tsagaadai, Sodbileg
     */
    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("admin123")) {
            loadDoctorsView();
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }
    /**
     * @description Login amjilttai bolson uyd doctor view-iig achaallana.
     *
     * @return void
     * @throws IOException here tuhain doctoriin fxml oldohgui uyd exception butsaana.
     */
    public void loadDoctorsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(DOCTOR_HOME_VIEW));
            AnchorPane doctorsView = loader.load();

            Scene doctorsScene = new Scene(doctorsView);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(doctorsScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @description "Нууц үг сэргээх" holboos deer darah eventiig hiih .
     * forgot-password-view ruu shiljine.
     *
     * @param event holboos event-iin uildel.
     * @return void
     * @throws IOException herwee FXML file forgot-password-view baihgui bol exception butsana.
     */
    @FXML
    public void handleForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FORGOT_PASSWORD_VIEW));
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
