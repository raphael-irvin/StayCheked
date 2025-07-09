package com.example.staycheked;

import com.example.staycheked.controller.main.UserAuthController;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.service.UserAuthService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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
        System.out.println("DEBUG - " + className + ": " + message);
    }

    public static void main(String[] args) {
        launch();
    }
}