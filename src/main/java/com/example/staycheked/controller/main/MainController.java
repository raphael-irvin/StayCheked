package com.example.staycheked.controller.main;

import java.util.ArrayList;
import java.util.Optional;

import com.example.staycheked.Main;
import com.example.staycheked.Session;
import com.example.staycheked.controller.lists.*;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Admin;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainController {

    @FXML
    BorderPane mainBorderPane;

    //Navbars
    @FXML
    Button signOutNavbar;
    @FXML
    Button dashboardNavbar;

    //Dashboard Buttons
    @FXML
    Button ticketButton;
    @FXML
    Button bookingButton;
    @FXML
    Button userListButton;

    //Action Bars
    @FXML
    Button verifyBookingButton;
    @FXML
    Button newSupportTicketButton;
    @FXML
    Label userInfoLabel;

    //Services
    BookingAuthService bookingAuthService;
    TicketService ticketService;

    Button[] navButtons;

    public void initialize() {
        signOutNavbar.setOnAction(this::onNavbarSignOutClick);

        //ACTION BAR SETUP
        verifyBookingButton.setOnAction(e -> showVerifyBookingDialog(e));
        newSupportTicketButton.setOnAction(e -> showNewSupportTicketDialog(e));
        userInfoLabel.setText("Logged In as: " + Session.getCurrentUser().getUsername());

        if (!(Session.getCurrentUser() instanceof Guest)) {
            // Hide the verify booking button for non-guest users
            verifyBookingButton.setVisible(false);
            newSupportTicketButton.setVisible(false);
        }

        //DASHBOARD SETUP
        //USER TYPE: Accommodation
        if (Session.getCurrentUser() instanceof Accommodation) {
            navButtons = new Button[3];
            navButtons[0] = ticketButton;
            navButtons[1] = bookingButton;
            navButtons[2] = userListButton;
        } 
            
        //USER TYPE: Guest
        else if (Session.getCurrentUser() instanceof Guest) {
            userListButton.setText("Accommodations");

            navButtons = new Button[3];
            navButtons[0] = ticketButton;
            navButtons[1] = bookingButton;
            navButtons[2] = userListButton;
        } 
            
        //USER TYPE: Admin
        else if (Session.getCurrentUser() instanceof Admin) {
            userListButton.setText("Verifications");
            if (ticketButton.getParent() != null) {
                ((javafx.scene.layout.Pane) ticketButton.getParent()).getChildren().remove(ticketButton);
            }
            if (bookingButton.getParent() != null) {
                ((javafx.scene.layout.Pane) bookingButton.getParent()).getChildren().remove(bookingButton);
            }
            navButtons = new Button[1];
            navButtons[0] = userListButton;
        } 
        
        else {
            Main.debug("MainController", "Unknown user type.");
        }
    }

    //Inject the BookingAuthService and TicketService into the MainController
    public MainController(BookingAuthService bookingAuthService, TicketService ticketService) {
        this.bookingAuthService = bookingAuthService;
        this.ticketService = ticketService;
    }
    
    //NAVBAR FUNCTIONS
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

    //DASHBOARD FUNCTIONS
    public void onTicketButtonClick(ActionEvent event) {
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
            FXMLLoader fxmlLoader = null;
            if (Session.getCurrentUser() instanceof Guest) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/GuestBookingView.fxml"));
            } else if (Session.getCurrentUser() instanceof Accommodation) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/AccommodationBookingView.fxml"));
            } else {
                Main.debug("MainController", "Unknown user type for booking view.");
                return;
            }
            fxmlLoader.setController(new BookingListController());
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(null);
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Booking View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onUserListButtonClick(ActionEvent event) {
        onNavbarButtonClick(event);
        try {
            FXMLLoader fxmlLoader = null;
            if (Session.getCurrentUser() instanceof Guest || Session.getCurrentUser() instanceof Admin) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/AccommodationView.fxml"));
                fxmlLoader.setController(new AccommodationListController());
            } else if (Session.getCurrentUser() instanceof Accommodation) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staycheked/views/ContentViews/ListViews/GuestView.fxml"));
                fxmlLoader.setController(new GuestListController());
            } else {
                Main.debug("MainController", "Unknown user type for user list view.");
                return;
            }
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading User List View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //ACTION BAR FUNCTIONS
    //Verify Booking Dialog
    private void showVerifyBookingDialog(ActionEvent event) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Verify Booking");
        dialog.setHeaderText("Enter Last Name and Booking ID");

        ButtonType verifyButtonType = new ButtonType("Verify", ButtonType.OK.getButtonData());
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(verifyButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField bookingIdField = new TextField();
        bookingIdField.setPromptText("Booking ID");

        grid.add(new Label("Last Name:"), 0, 0);
        grid.add(lastNameField, 1, 0);
        grid.add(new Label("Booking ID:"), 0, 1);
        grid.add(bookingIdField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == verifyButtonType) {
                return new Pair<>(lastNameField.getText(), bookingIdField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user to respond
        // This will block until the dialog is closed or a button is clicked
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            String lastName = pair.getKey();
            String bookingId = pair.getValue();
            if (bookingAuthService.verifyBooking(bookingId, lastName) != null) {
                // Successful Verification Dialog
                Dialog<Void> successDialog = new Dialog<>();
                successDialog.setTitle("Verification Success");
                successDialog.setHeaderText(null);
                successDialog.setContentText("Successful Verification");
                successDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                successDialog.showAndWait();
                Main.debug("GuestActionBarController", "Booking verified for " + lastName + " with Booking ID: " + bookingId);
            } else {
                // Failed Verification Dialog
                Dialog<Void> successDialog = new Dialog<>();
                successDialog.setTitle("Failed Verification");
                successDialog.setHeaderText(null);
                successDialog.setContentText("Failed Verification. Please check your details.");
                successDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                successDialog.showAndWait();
                Main.debug("GuestActionBarController", "Booking verification failed.");
            }
        });
    }

    private void showNewSupportTicketDialog(ActionEvent event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("New Support Ticket");
        dialog.setHeaderText("Submit a new support ticket");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonType.OK.getButtonData());
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Booking selection ComboBox
        javafx.scene.control.ComboBox<String> bookingComboBox = new javafx.scene.control.ComboBox<>();
        bookingComboBox.setPromptText("Select Booking");

        //Get verified Bookings based on logged user
        ArrayList<Booking> verifiedBookings = BookingAuthService.getVerifiedBookingsBasedOnLoggedUser(Session.getCurrentUser());
        ArrayList<Booking> openBookings = new ArrayList<>();

        for (Booking booking : verifiedBookings) {
            Main.debug("GuestActionBarController", "Matching booking: " + booking.getBookingID() + " - " + booking.getAccommodation().getAccommodationName());
            Main.debug("GuestActionBarController", "Booking status: " + booking.getStatus());
            if (booking.getStatus().equalsIgnoreCase("Verified")) {
                Main.debug("GuestActionBarController", "Open Booking Detected: " + booking.getBookingID() + " - " + booking.getAccommodation().getAccommodationName());
                // Remove booking if not verified
                openBookings.add(booking);
            }
        }

        bookingComboBox.getItems().addAll(
            openBookings.stream()
                .map(booking -> booking.getBookingID() + " - " + booking.getAccommodation().getAccommodationName())
                .toList()
        );

        // Category ComboBox
        javafx.scene.control.ComboBox<String> categoryComboBox = new javafx.scene.control.ComboBox<>();
        categoryComboBox.getItems().addAll(
            "Broken Utilities (Maintenance)",
            "Internet Facilities",
            "Housekeeping",
            "Others/Unspecified"
        );
        categoryComboBox.setPromptText("Select Category");

        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");
        TextField enquiryField = new TextField();
        enquiryField.setPromptText("Enquiry");

        grid.add(new Label("Booking:"), 0, 0);
        grid.add(bookingComboBox, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryComboBox, 1, 1);
        grid.add(new Label("Subject:"), 0, 2);
        grid.add(subjectField, 1, 2);
        grid.add(new Label("Enquiry:"), 0, 3);
        grid.add(enquiryField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Add event filter to Confirm button to prevent dialog closing on invalid input
        Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.addEventFilter(javafx.event.ActionEvent.ACTION, evt -> {
            String selectedBooking = bookingComboBox.getValue();
            String selectedCategory = categoryComboBox.getValue();
            String subject = subjectField.getText();
            String enquiry = enquiryField.getText();
            if (selectedBooking == null || selectedCategory == null || subject.isEmpty() || enquiry.isEmpty()) {
                Dialog<Void> errorDialog = new Dialog<>();
                errorDialog.setTitle("Error");
                errorDialog.setHeaderText(null);
                errorDialog.setContentText("Please fill in all fields.");
                errorDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                errorDialog.showAndWait();
                evt.consume(); // Prevent dialog from closing
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                String selectedBooking = bookingComboBox.getValue();
                String selectedCategory = categoryComboBox.getValue();
                String subject = subjectField.getText();
                String enquiry = enquiryField.getText();
                // Only create ticket if all fields are filled (event filter ensures this)
                if (selectedBooking != null && selectedCategory != null && !subject.isEmpty() && !enquiry.isEmpty()) {
                    ticketService.postNewTicket(
                        DataStore.findBookingByID(selectedBooking.split(" - ")[0]).getAccommodation().getEmailAddress(),
                        Session.getCurrentUser().getEmailAddress(),
                        DataStore.findBookingByID(selectedBooking.split(" - ")[0]),
                        subject,
                        selectedCategory,
                        enquiry
                    );

                    Main.debug("GuestActionBarController", "Support ticket created: " + selectedBooking + ", " + selectedCategory + ", " + subject + ", " + enquiry);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    //UTILITY / TRANSITION FUNCTIONS
    private void onNavbarButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        for (Button button : navButtons) {
            if (button.equals(clickedButton)) {
                if (button.getStyleClass().contains("DashboardButton")) {
                    button.getStyleClass().remove("DashboardButton");
                } 
                
                if (button.getStyleClass().contains("DashboardButton-Active")) {
                    return; // Already active, no need to change
                }
                
                button.getStyleClass().add("DashboardButton-Active");

            } 
            
            else {
                if (button.getStyleClass().contains("DashboardButton-Active")) {
                    button.getStyleClass().remove("DashboardButton-Active");
                }
                button.getStyleClass().add("DashboardButton");
            }
        }
    }
}
