package com.example.staycheked.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    BorderPane mainBorderPane = new BorderPane();

    @FXML
    Button stayCheckedNavbar = new Button("StayChecked");

    @FXML
    Button loginNavbar = new Button("Login");

    @FXML
    Button registerNavbar = new Button("Register");
}
