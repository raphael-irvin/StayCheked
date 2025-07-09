package com.example.staycheked.controller.main;

import com.example.staycheked.Main;
import com.example.staycheked.Session;
import com.example.staycheked.controller.actionBars.GuestActionBarController;
import com.example.staycheked.controller.lists.*;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.service.BookingAuthService;
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
    Button bookingButton;
    @FXML
    Button guestButton;

    //Dashboard buttons for Guest
    @FXML
    Button guestTicketButton;
    @FXML
    Button guestBookingButton;
    @FXML
    Button accommodationButton;

    Button[] navButtons;

    public void initialize() {
        signOutNavbar.setOnAction(this::onNavbarSignOutClick);
        if (Session.getCurrentUser() != null) {
            if (Session.getCurrentUser() instanceof Accommodation) {
                navButtons = new Button[3];
                navButtons[0] = accommodationTicketButton;
                navButtons[1] = guestButton;
                navButtons[2] = bookingButton;
            } else if (Session.getCurrentUser() instanceof Guest) {
                navButtons = new Button[3];
                navButtons[0] = guestTicketButton;
                navButtons[1] = guestBookingButton;
                navButtons[2] = accommodationButton;
            } else {
                Main.debug("MainController", "Unknown user type.");
            }
        } else {
            Main.debug("MainController", "No user is currently logged in.");
        }

        //Initialize Bottom Action Bar
        if (Session.getCurrentUser() instanceof Guest) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ActionBars/GuestActionBar.fxml"));
            fxmlLoader.setController(new GuestActionBarController(new BookingAuthService(), new TicketService()));
            try {
                Parent root = fxmlLoader.load();
                mainBorderPane.setBottom(root);
            } catch (Exception e) {
                Main.debug("MainController", "Error loading Guest Action Bar: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public void onNavbarSignOutClick(ActionEvent event) {
        Session.clear();
        Main.debug("MainController", "Sign out clicked");
        // Redirect to login view or perform other actions
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/AuthenticationViews/LoginView.fxml"));
        fxmlLoader.setController(new UserAuthController(new UserAuthService()));
        try {
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            Main.debug("MainController", "Redirected to login view after sign out.");
        } catch (Exception e) {
            Main.debug("MainController", "Error loading login view: " + e.getMessage());
        }
    }

    //Functions for Accommodation Dashboard
    public void onAccommodationTicketButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/TicketView.fxml"));
            fxmlLoader.setController(new TicketListController(new TicketService()));
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Active Ticket View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onBookingButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/AccommodationBookingView.fxml"));
            fxmlLoader.setController(new BookingListController());
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(null);
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Booking View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onGuestButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/GuestView.fxml"));
            fxmlLoader.setController(new GuestListController());
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Guest View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Functions for Guest Dashboard
    public void onGuestTicketButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/TicketView.fxml"));
            fxmlLoader.setController(new TicketListController(new TicketService()));
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Guest Ticket View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onGuestBookingButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/GuestBookingView.fxml"));
            fxmlLoader.setController(new BookingListController());
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(null);
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Guest Booking View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onAccommodationButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/AccommodationView.fxml"));
            fxmlLoader.setController(new AccommodationListController());
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Guest View: " + e.getMessage());
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
