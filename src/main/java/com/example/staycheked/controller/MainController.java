package com.example.staycheked.controller;

import com.example.staycheked.Session;
import com.example.staycheked.service.UserAuthService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

    @FXML
    BorderPane mainBorderPane = new BorderPane();

    @FXML
    Button aboutNavbar;

    @FXML
    Button signOutNavbar;

    @FXML
    Button dashboardNavbar;

    public void initialize() {
        signOutNavbar.setOnAction(this::onNavbarSignOutClick);
    }
    
    public void onNavbarSignOutClick(ActionEvent event) {
        Session.clear();
        System.out.println("Sign out clicked");
        // Redirect to login view or perform other actions
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/LoginView.fxml"));
        fxmlLoader.setController(new UserAuthController(new UserAuthService()));
        try {
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            System.out.println("Redirected to login view after sign out.");
        } catch (Exception e) {
            System.out.println("Error loading login view: " + e.getMessage());
        }
    }
}
