package com.example.staycheked.controller;

import com.example.staycheked.Session;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.model.user.User;
import com.example.staycheked.service.UserAuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UserAuthController {

    private UserAuthService userAuthService;

    @FXML
    TextField emailField;

    @FXML
    TextField passwordField;

    @FXML
    Button loginButton;

    @FXML
    Button registerGuestButton;

    @FXML
    Button registerAccommodationButton;

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

    public void onRegisterGuestButtonClick(String username, String emailAddress, String contactNo, String password, String fullName) {

        Guest guest = userAuthService.registerGuest(username, emailAddress, contactNo, password, fullName);

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

}
