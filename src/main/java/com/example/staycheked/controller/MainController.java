package com.example.staycheked.controller;

import com.example.staycheked.Session;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.service.TicketService;
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
    Button signOutNavbar;

    @FXML
    Button dashboardNavbar;

    //Dashboard buttons for Accomodation
    @FXML
    Button accommodationTicketButton;
    @FXML
    Button guestButton;

    //Dashboard buttons for Guest
    @FXML
    Button guestTicketButton;
    @FXML
    Button accommodationButton;

    Button[] navButtons = new Button[2];

    public void initialize() {
        signOutNavbar.setOnAction(this::onNavbarSignOutClick);
        if (Session.getCurrentUser() != null) {
            if (Session.getCurrentUser() instanceof Accommodation) {
                navButtons[0] = accommodationTicketButton;
                navButtons[1] = guestButton;
            } else if (Session.getCurrentUser() instanceof Guest) {
                navButtons[0] = guestTicketButton;
                navButtons[1] = accommodationButton;
            } else {
                System.out.println("Unknown user type.");
            }
        } else {
            System.out.println("No user is currently logged in.");
        }
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

    //Functions for Accommodation Dashboard
    public void onAccommodationTicketButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ticketView.fxml"));
            fxmlLoader.setController(new TicketController(new TicketService()));
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            System.out.println("Error loading Active Ticket View: " + e.getMessage());
        }
    }

    public void onGuestButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/GuestView.fxml"));
            fxmlLoader.setController(new GuestController());
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            System.out.println("Error loading Guest View: " + e.getMessage());
        }
    }

    //Functions for Guest Dashboard
    public void onGuestTicketButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ticketView.fxml"));
            fxmlLoader.setController(new TicketController(new TicketService()));
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            System.out.println("Error loading Guest Ticket View: " + e.getMessage());
        }
    }

        public void onAccommodationButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/AccommodationView.fxml"));
            fxmlLoader.setController(new AccommodationController());
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            System.out.println("Error loading Guest View: " + e.getMessage());
        }
    }

    private void onNavbarButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        for (Button button : navButtons) {
            if (button.equals(clickedButton)) {
                button.getStyleClass().remove("DashboardButton");
                button.getStyleClass().add("DashboardButton-Active");
            } else {
                if (button.getStyleClass().contains("DashboardButton-Active")) {
                    button.getStyleClass().remove("DashboardButton-Active");
                }
                button.getStyleClass().add("DashboardButton");
            }
        }
    }
}
