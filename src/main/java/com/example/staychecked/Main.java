package com.example.staychecked;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.staychecked.controller.main.UserAuthController;
import com.example.staychecked.model.DataStore;
import com.example.staychecked.service.UserAuthService;
import com.example.staychecked.service.UtilService;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        debug("Main", "Current Java Version: " + System.getProperty("java.version"));
        //Data Initialization
        DataStore.dataInitialization();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/AuthenticationViews/LoginView.fxml"));
        UserAuthController userAuthController = new UserAuthController(new UserAuthService());
        fxmlLoader.setController(userAuthController);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("StayChecked");
        stage.setScene(scene);
        stage.show();
    }

    // Centralized debug output method
    public static void debug(String className, String message) {
        String debugLine = "DEBUG - " + className + ": " + message;
        System.out.println(debugLine);
        UtilService.writeDebugLogToFile(debugLine); // Write to debug log file
    }

    public static void main(String[] args) {
        launch();
    }
}