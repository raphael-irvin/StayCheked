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
    BorderPane mainBorderPane;

    @FXML
    Button aboutNavbar;

    @FXML
    Button signOutNavbar;

    @FXML
    Button dashboardNavbar;

    Button[] navButtons = new Button[3];

    public void initialize() {
        signOutNavbar.setOnAction(this::onNavbarSignOutClick);
        navButtons[0] = aboutNavbar; navButtons[1] = signOutNavbar; navButtons[2] = dashboardNavbar;
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

    private void onNavbarButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        for (Button button : navButtons) {
            if (button.equals(clickedButton)) {
                button.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
            } else {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
            }
        }
    }
}
