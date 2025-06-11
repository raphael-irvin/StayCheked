package com.example.staycheked.controller;

import com.example.staycheked.Session;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.model.user.User;
import com.example.staycheked.service.UserAuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UserAuthController {

    private UserAuthService userAuthService;
    private Stage stage;

    @FXML
    TextField emailField;

    @FXML
    TextField passwordField;

    @FXML
    TextField fullNameField;

    @FXML
    TextField usernameField;

    @FXML
    TextField contactNoField;

    @FXML
    TextField accommodationNameField;

    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onLoginButtonClick() {
        User user = userAuthService.login(emailField.getText(), passwordField.getText());

        if (user != null) {
            System.out.println("Login successful!");
            Session.setCurrentUser(user);
            // Redirect to the main application view
        } else {
            System.out.println("Login failed. Please check your credentials.");
            // Show error message in the UI
        }
    }

    public void onRegisterGuestButtonClick() {

        Guest guest = userAuthService.registerGuest(
                usernameField.getText(),
                emailField.getText(),
                contactNoField.getText(),
                passwordField.getText(),
                fullNameField.getText()
        );

        if (guest != null) {
            System.out.println("Guest registration successful!");
            Session.setCurrentUser(guest);
            // Redirect to the main application view
        } else {
            System.out.println("Guest registration failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void onRegisterAccommodationButtonClick(String username, String emailAddress, String contactNo, String password, String accommodationName, String location) {

        Accommodation accommodation = userAuthService.registerAccommodation(username, emailAddress, contactNo, password, accommodationName, location);

        if (accommodation != null) {
            System.out.println("Accommodation registration successful!");
            Session.setCurrentUser(accommodation);
            // Redirect to the main application view
        } else {
            System.out.println("Accommodation registration failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void switchToRegisterGuestView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/RegisterView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            System.out.println("Switching back to login view.");
        } catch (IOException e) {
            System.out.println("Error loading login view: " + e.getMessage());
            System.out.println("Switching back to login view.");
        }
    }

    public void backToLoginButtonClick() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/LoginView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            System.out.println("Switching back to login view.");
        } catch (IOException e) {
            System.out.println("Error loading login view: " + e.getMessage());
            System.out.println("Switching back to login view.");
        }
    }
}
