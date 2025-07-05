package com.example.staycheked.controller;

import com.example.staycheked.Session;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.model.user.User;
import com.example.staycheked.service.UserAuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;

public class UserAuthController {

    private UserAuthService userAuthService;

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

    @FXML
    TextField accommodationAddressField;

    @FXML
    ToggleButton toggleAccommodationRegistration;

    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
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

    public void onRegisterButtonClick() {
        if (usernameField.getText().isEmpty() || emailField.getText().isEmpty() ||
            contactNoField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showErrorAlert("Please fill in all required fields.");
            System.out.println("User registration failed. Missing required fields.");
            return;
        }

        //Accommodation registration
        if (toggleAccommodationRegistration.isSelected()) {
            if (accommodationNameField.getText().isEmpty() || accommodationAddressField.getText().isEmpty()) {
                showErrorAlert("Please fill in all required fields for accommodation registration.");
                System.out.println("User registration failed. Missing required fields for accommodation registration.");
                return;
            }
            onRegisterAccommodationButtonClick();
        }

        //Guest registration
        if (!toggleAccommodationRegistration.isSelected()) {
            if (fullNameField.getText().isEmpty()) {
                showErrorAlert("Please fill in all required fields for guest registration.");
                System.out.println("User registration failed. Missing required fields for guest registration.");
                return;
            }
            onRegisterGuestButtonClick();
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

    public void onRegisterAccommodationButtonClick() {

        Accommodation accommodation = userAuthService.registerAccommodation(
                usernameField.getText(),
                emailField.getText(),
                contactNoField.getText(),
                passwordField.getText(),
                accommodationNameField.getText(),
                accommodationAddressField.getText()
        );

        if (accommodation != null) {
            System.out.println("Accommodation registration successful!");
            Session.setCurrentUser(accommodation);
            // Redirect to the main application view
        } else {
            System.out.println("Accommodation registration failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void switchToRegisterGuestView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/RegisterGuestView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            System.out.println("Switching to register guest view.");
        } catch (IOException e) {
            System.out.println("Error loading login view: " + e.getMessage());
        }
    }

    public void switchToRegisterAccommodationView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/RegisterAccommodationView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            toggleAccommodationRegistration.setSelected(true);
            stage.setScene(scene);
            System.out.println("Switching to register accommodation view.");
        } catch (IOException e) {
            System.out.println("Error loading register accommodation view: " + e.getMessage());
        }
    }

    public void backToLoginButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

    public void onToggleAccommodationRegistrationButtonClick(ActionEvent event) {
        if (toggleAccommodationRegistration.isSelected()) {
            switchToRegisterAccommodationView(event);
        } else if (!toggleAccommodationRegistration.isSelected()) {
            switchToRegisterGuestView(event);
        }
    }

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
