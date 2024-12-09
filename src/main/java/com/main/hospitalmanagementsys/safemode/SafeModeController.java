package com.main.hospitalmanagementsys.safemode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SafeModeController {

    @FXML
    private void exitSafeMode(ActionEvent event) {
        SafeModeHandler.deactivateSafeMode();
        showAlert("You have exited Safe Mode. Full functionality is restored.");

    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Safe Mode");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
