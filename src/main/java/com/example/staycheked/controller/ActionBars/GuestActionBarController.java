package com.example.staycheked.controller.ActionBars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Optional;

import com.example.staycheked.Session;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.service.BookingAuthService;
import com.example.staycheked.service.TicketService;

public class GuestActionBarController {

    @FXML
    Button verifyBookingButton;
    @FXML
    Button newSupportTicketButton;

    BookingAuthService bookingAuthService;
    TicketService ticketService;

    public GuestActionBarController(BookingAuthService bookingAuthService, TicketService ticketService) {
        this.bookingAuthService = bookingAuthService;
        this.ticketService = ticketService;
    }

    public void initialize() {
        verifyBookingButton.setOnAction(e -> showVerifyBookingDialog(e));
        newSupportTicketButton.setOnAction(e -> showNewSupportTicketDialog(e));
    }

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
                System.out.println("Booking verified for " + lastName + " with Booking ID: " + bookingId);
            } else {
                // Failed Verification Dialog
                Dialog<Void> successDialog = new Dialog<>();
                successDialog.setTitle("Failed Verification");
                successDialog.setHeaderText(null);
                successDialog.setContentText("Failed Verification. Please check your details.");
                successDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                successDialog.showAndWait();
                System.out.println("Booking verification failed.");
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
        bookingComboBox.getItems().addAll(
            BookingAuthService.getVerifiedBookingsBasedOnLoggedUser(Session.getCurrentUser()).stream()
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

                    System.out.println("Support ticket created: " + selectedBooking + ", " + selectedCategory + ", " + subject + ", " + enquiry);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}
