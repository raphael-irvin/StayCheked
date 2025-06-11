package com.example.staycheked;

import com.example.staycheked.controller.UserAuthController;
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

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/LoginView.fxml"));
        UserAuthController userAuthController = new UserAuthController();
        fxmlLoader.setController(userAuthController);
        userAuthController.setStage(stage);
        userAuthController.setUserAuthService(new UserAuthService());
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("StayChecked");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}