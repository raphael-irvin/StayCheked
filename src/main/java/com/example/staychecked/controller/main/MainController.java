package com.example.staychecked.controller.main;

import java.util.ArrayList;
import java.util.Optional;

import com.example.staychecked.Main;
import com.example.staychecked.Session;
import com.example.staychecked.controller.lists.*;
import com.example.staychecked.model.DataStore;
import com.example.staychecked.model.object.Booking;
import com.example.staychecked.model.user.Accommodation;
import com.example.staychecked.model.user.Admin;
import com.example.staychecked.model.user.Guest;
import com.example.staychecked.service.BookingAuthService;
import com.example.staychecked.service.ChatbotService;
import com.example.staychecked.service.TicketService;
import com.example.staychecked.service.UserAuthService;

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
    @FXML
    Button helpNavbar;

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

    Button[] navbarButtons;
    Button[] dashboardButtons;

    public void initialize() {
        dashboardNavbar.setOnAction(this::onNavbarDashboardClick);
        signOutNavbar.setOnAction(this::onNavbarSignOutClick);
        helpNavbar.setOnAction(this::onNavbarHelpClick);

        //NAVBAR SETUP
        navbarButtons = new Button[3];
        navbarButtons[0] = signOutNavbar;
        navbarButtons[1] = dashboardNavbar;
        navbarButtons[2] = helpNavbar;

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
            dashboardButtons  = new Button[3];
            dashboardButtons [0] = ticketButton;
            dashboardButtons [1] = bookingButton;
            dashboardButtons [2] = userListButton;
        } 
            
        //USER TYPE: Guest
        else if (Session.getCurrentUser() instanceof Guest) {
            userListButton.setText("Accommodations");

            dashboardButtons  = new Button[3];
            dashboardButtons [0] = ticketButton;
            dashboardButtons [1] = bookingButton;
            dashboardButtons [2] = userListButton;
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
            dashboardButtons  = new Button[1];
            dashboardButtons [0] = userListButton;
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
    public void onNavbarDashboardClick(ActionEvent event) {
        Main.debug("MainController", "Dashboard clicked");
        onNavbarButtonClick(event);
        // Load the dashboard view
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/MainViews/MainView.fxml"));
            fxmlLoader.setController(new MainController(bookingAuthService, ticketService));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Dashboard View: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void onNavbarSignOutClick(ActionEvent event) {
        Session.clear();
        Main.debug("MainController", "Sign out clicked");
        // Redirect to login view or perform other actions
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/AuthenticationViews/LoginView.fxml"));
        fxmlLoader.setController(new UserAuthController(new UserAuthService()));
        try {
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            Main.debug("MainController", "Redirected to login view after sign out.");
        } catch (Exception e) {
            Main.debug("MainController", "Error loading login view: " + e.getMessage());
        }
    }

    public void onNavbarHelpClick(ActionEvent event) {
        Main.debug("MainController", "Help clicked");
        onNavbarButtonClick(event);
        // Load the help view with error handling for ChatbotService
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/MainViews/HelpPageView.fxml"));
            
            //Show Loading Dialog until ChatbotService is initialized
            Dialog<String> loadingDialog = new Dialog<>();
            loadingDialog.setTitle("Loading Chatbot Service");
            loadingDialog.setHeaderText("The Chabot Service has been initialized. Please wait while the help page loads.");
            loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            loadingDialog.show();

            // Try to create ChatbotService, fall back to null if it fails
            ChatbotService chatbotService = null;
            try {
                chatbotService = new ChatbotService();
                Main.debug("MainController", "ChatbotService initialized successfully");
            } catch (Exception chatbotError) {
                Main.debug("MainController", "ChatbotService initialization failed: " + chatbotError.getMessage());
                Main.debug("MainController", "Help page will load without chatbot functionality");
            }
            
            fxmlLoader.setController(new HelpPageController(chatbotService));
            Parent root = fxmlLoader.load();
            mainBorderPane.setLeft(null);
            mainBorderPane.setCenter(null);
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Help View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //DASHBOARD FUNCTIONS
    public void onTicketButtonClick(ActionEvent event) {
        onDashboardButtonClick(event);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/ContentViews/ListViews/TicketView.fxml"));
            fxmlLoader.setController(new TicketListController(new TicketService()));
            Parent root = fxmlLoader.load();
            mainBorderPane.setCenter(root);
        } catch (Exception e) {
            Main.debug("MainController", "Error loading Active Ticket View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onBookingButtonClick(ActionEvent event) {
        onDashboardButtonClick(event);
        try {
            FXMLLoader fxmlLoader = null;
            if (Session.getCurrentUser() instanceof Guest) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/ContentViews/ListViews/GuestBookingView.fxml"));
            } else if (Session.getCurrentUser() instanceof Accommodation) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/ContentViews/ListViews/AccommodationBookingView.fxml"));
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
        onDashboardButtonClick(event);
        try {
            FXMLLoader fxmlLoader = null;
            if (Session.getCurrentUser() instanceof Guest || Session.getCurrentUser() instanceof Admin) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/ContentViews/ListViews/AccommodationView.fxml"));
                fxmlLoader.setController(new AccommodationListController());
            } else if (Session.getCurrentUser() instanceof Accommodation) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/StayChecked/views/ContentViews/ListViews/GuestView.fxml"));
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
    private void onDashboardButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        for (Button button : dashboardButtons) {
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

    public void onNavbarButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        for (Button button : navbarButtons) {
            if (button.equals(clickedButton)) {
                if (button.getStyleClass().contains("navbarActive")) {
                    return; // Already active, no need to change
                }
                button.getStyleClass().remove("navbarInactive");
                button.getStyleClass().add("navbarActive");
            } else {
                button.getStyleClass().remove("navbarActive");
                button.getStyleClass().add("navbarInactive");
            }
        }
    }

}
