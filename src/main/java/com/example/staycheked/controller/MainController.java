package com.example.staycheked.controller;

import com.example.staycheked.service.UserAuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    BorderPane mainBorderPane = new BorderPane();

    @FXML
    Button stayCheckedNavbar = new Button("StayChecked");

    @FXML
    Button loginNavbar = new Button("Login");

    @FXML
    Button registerNavbar = new Button("Register");

    List<Button> navbarButtons = List.of(stayCheckedNavbar, loginNavbar, registerNavbar);


    public void switchToLogin(ActionEvent event) throws IOException {
        mainBorderPane.setCenter(null);

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("/com/example/staycheked/views/LoginView.fxml"));

        UserAuthController userAuthController = new UserAuthController();
        userAuthController.setUserAuthService(new UserAuthService());
        loginLoader.setController(userAuthController);

        Node loginView = loginLoader.load();
        mainBorderPane.setCenter(loginView);
    }

}
