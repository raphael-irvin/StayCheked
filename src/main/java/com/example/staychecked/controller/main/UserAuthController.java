package com.example.staychecked.controller.main;

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

import com.example.staychecked.Main;
import com.example.staychecked.Session;
import com.example.staychecked.model.user.Accommodation;
import com.example.staychecked.model.user.Guest;
import com.example.staychecked.model.user.User;
import com.example.staychecked.service.BookingAuthService;
import com.example.staychecked.service.TicketService;
import com.example.staychecked.service.UserAuthService;

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

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    public void onLoginButtonClick(ActionEvent event) {
        User user = userAuthService.login(emailField.getText(), passwordField.getText());

        if (user != null) {
            Main.debug("UserAuthController", "Login successful!");
            Session.setCurrentUser(user);
            redirectToMainView(event);
        } 
        
        //CASE: Login failed
        else {
            Main.debug("UserAuthController", "Login failed. Please check your credentials.");
            // Show error message in the UI
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Login Failed");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Login failed. Please check your email and password.");
            errorAlert.showAndWait();
        }
    }

    public void onRegisterButtonClick(ActionEvent event) {
        Main.debug("UserAuthController", "Register button clicked. Starting registration process...");

        //FAIL CONDITION 1: Missing required fields
        if (usernameField.getText().isEmpty() || emailField.getText().isEmpty() ||
            contactNoField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showErrorAlert("Please fill in all required fields.");
            Main.debug("UserAuthController", "User registration failed. Missing required fields.");
            return;
        }
        
        //FAIL CONDITION 2: Format Errors

        //Validate Username Format
        String username = usernameField.getText();
        if (username.length() < 5 || username.length() > 20 ||
            !username.matches("^[a-zA-Z][a-zA-Z0-9._]*$") ||
            username.contains("..") || username.contains("__") ||
            username.contains(" ")) {
            showErrorAlert("Invalid username. It must be:\n" +
                "* 5-20 characters long\n" +
                "* Start with a letter\n" +
                "* Can only include letters, numbers, underscores, or dots\n" +
                "* Cannot contain consecutive special characters like '__' or '..'\n" +
                "* Cannot contain spaces");
            Main.debug("UserAuthController", "User registration failed due to invalid username format.");
            return;
        }

        //Validate Email Format
        String email = emailField.getText();
        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showErrorAlert("Invalid email address. It must be:\n" +
                "* A valid email format (e.g., user@example.com)\n" +
                "* Contain a domain name and a top-level domain (e.g., .com, .org)");
            Main.debug("UserAuthController", "User registration failed due to invalid email format.");
            return;
        }

        //Password Validation
        String password = passwordField.getText();
        if (password.length() < 8 ||
            !password.matches(".*[A-Z].*") ||
            !password.matches(".*[a-z].*") ||
            !password.matches(".*\\d.*") ||
            !password.matches(".*[!@#$%^&*].*") ||
            password.contains(" ") ||
            password.toLowerCase().contains(username.toLowerCase()) ||
            password.matches(".*(password|123456|qwerty|letmein|welcome).*")) {
            showErrorAlert("Invalid password. It must be:\n" +
                "* At least 8 characters long (12 recommended)\n" +
                "* Include an uppercase letter\n" +
                "* Include a lowercase letter\n" +
                "* Include a number\n" +
                "* Include a special character (!@#$%^&*)\n" +
                "* Not contain spaces\n" +
                "* Not be similar to the username or common passwords");
            Main.debug("UserAuthController", "User registration failed due to invalid password format.");
            return;
        }

        //Contact Number Validation
        String contactNo = contactNoField.getText().replaceAll("[\\s()-]", ""); // Remove spaces, dashes, and parentheses
        if (!contactNo.matches("^\\+?\\d{10,15}$")) {
            showErrorAlert("Invalid contact number. It must be:\n" +
                "* A valid international phone number format (e.g., +60123456789)\n" +
                "* Contain only digits after the '+' (if present)\n" +
                "* Be between 10 to 15 digits (excluding the '+' sign)");
            Main.debug("UserAuthController", "User registration failed due to invalid contact number format.");
            return;
        }


        Main.debug("UserAuthController", "Accommodation Toggle Status: " + toggleAccommodationRegistration.isSelected());

        //CASE: Accommodation registration
        if (toggleAccommodationRegistration.isSelected()) {

            if (accommodationNameField.getText().isEmpty() || accommodationAddressField.getText().isEmpty()) {
                showErrorAlert("Please fill in all required fields for accommodation registration.");
                Main.debug("UserAuthController", "User registration failed. Missing required fields for accommodation registration.");
                return;
            }

            onRegisterAccommodationButtonClick(event);
        }

        //CASE: Guest registration
        if (!toggleAccommodationRegistration.isSelected()) {
            if (fullNameField.getText().isEmpty()) {
                showErrorAlert("Please fill in all required fields for guest registration.");
                Main.debug("UserAuthController", "User registration failed. Missing required fields for guest registration.");
                return;
            }
            onRegisterGuestButtonClick(event);
        }
    }

    public void onRegisterGuestButtonClick(ActionEvent event) {

        Guest guest = userAuthService.registerGuest(
                usernameField.getText(),
                emailField.getText(),
                contactNoField.getText(),
                passwordField.getText(),
                fullNameField.getText()
        );

        if (guest != null) {
            Main.debug("UserAuthController", "Guest registration successful!");
            Session.setCurrentUser(guest);
            redirectToMainView(event);
        } else {
            Main.debug("UserAuthController", "Guest registration failed. Please check your details.");
            // Show error message in the UI
            showErrorAlert("Guest registration failed. Please check your details.");
        }
    }

    public void onRegisterAccommodationButtonClick(ActionEvent event) {

        Accommodation accommodation = userAuthService.registerAccommodation(
                usernameField.getText(),
                emailField.getText(),
                contactNoField.getText(),
                passwordField.getText(),
                accommodationNameField.getText(),
                accommodationAddressField.getText()
        );

        if (accommodation != null) {
            Main.debug("UserAuthController", "Accommodation registration successful!");
            Session.setCurrentUser(accommodation);
            redirectToMainView(event);
        } else {
            Main.debug("UserAuthController", "Accommodation registration failed. Please check your details.");
            // Show error message in the UI
            showErrorAlert("Accommodation registration failed. Please check your details.");
        }
    }

    public void switchToRegisterGuestView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staychecked/views/AuthenticationViews/RegisterGuestView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Main.debug("UserAuthController", "Switching to register guest view.");
        } catch (IOException e) {
            Main.debug("UserAuthController", "Error loading login view: " + e.getMessage());
        }
    }

    public void switchToRegisterAccommodationView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staychecked/views/AuthenticationViews/RegisterAccommodationView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            toggleAccommodationRegistration.setSelected(true);
            stage.setScene(scene);
            Main.debug("UserAuthController", "Switching to register accommodation view.");
        } catch (IOException e) {
            Main.debug("UserAuthController", "Error loading register accommodation view: " + e.getMessage());
        }
    }

    public void backToLoginButtonClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staychecked/views/AuthenticationViews/LoginView.fxml"));
        fxmlLoader.setController(this);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Main.debug("UserAuthController", "Switching back to login view.");
        } catch (IOException e) {
            Main.debug("UserAuthController", "Error loading login view: " + e.getMessage());
            Main.debug("UserAuthController", "Switching back to login view.");
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

    public void redirectToMainView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staychecked/views/MainViews/MainView.fxml"));
        fxmlLoader.setController(new MainController(new BookingAuthService(), new TicketService()));
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            Main.debug("UserAuthController", "Redirected to main application view.");
        } catch (IOException e) {
            Main.debug("UserAuthController", "Error loading main view: " + e.getMessage());
        }
    }
}
