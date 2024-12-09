package com.main.hospitalmanagementsys;

import com.main.hospitalmanagementsys.safemode.DatabaseBackup;
import com.main.hospitalmanagementsys.safemode.SafeModeHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        if (SafeModeHandler.isInSafeMode()) {
            System.out.println("Warning: The application is running in Safe Mode..");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/main/hospitalmanagementsys/ui/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        SafeModeHandler.activateSafeMode();

        DatabaseBackup.scheduleBackup();

        launch();
    }
}

