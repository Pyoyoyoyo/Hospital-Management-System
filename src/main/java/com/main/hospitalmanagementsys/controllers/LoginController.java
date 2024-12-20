package com.main.hospitalmanagementsys.controllers;

import com.main.hospitalmanagementsys.database.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username and password cannot be empty.");
            return;
        }

        try {
            if (authenticateUser(username, password)) {
                loadDoctorsView();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while logging in.");
        }
    }
    /**
     * @description Authenticate user credentials against the database.
     *
     * @param username User's username.
     * @param password User's password.
     * @return true if credentials are valid, false otherwise.
     */
    private boolean authenticateUser(String username, String password) {
        String query = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Query executed successfully, checking results...");
            if (rs.next()) {
                String hashedPassword = rs.getString("password_hash");
                if (hashedPassword == null || hashedPassword.isEmpty()) {
                    System.out.println("Error: Password hash is null or empty for user: " + username);
                    return false;
                }

                // Verify the password using BCrypt
                boolean passwordMatch = BCrypt.checkpw(password, hashedPassword);
                System.out.println("Password Match: " + passwordMatch);
                return passwordMatch;
            } else {
                System.out.println("No user found with username: " + username);
            }
        } catch (Exception e) {
            System.err.println("Error occurred during authentication: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @description Login amjilttai bolson uyd doctor view-iig achaallana.
     *
     * @return void
     * @throws IOException here tuhain doctoriin fxml oldohgui uyd exception butsaana.
     */
    private void loadDoctorsView() {
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
    /**
     * @description Show an alert dialog with a given type, title, and message.
     *
     * @param alertType Type of the alert.
     * @param title     Title of the alert.
     * @param message   Message of the alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
