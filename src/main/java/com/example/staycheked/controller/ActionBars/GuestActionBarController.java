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

import com.example.staycheked.service.BookingAuthService;

public class GuestActionBarController {

    @FXML
    Button verifyBookingButton;

    BookingAuthService bookingAuthService;

    public GuestActionBarController(BookingAuthService bookingAuthService) {
        this.bookingAuthService = bookingAuthService;
    }

    public void initialize() {
        verifyBookingButton.setOnAction(e -> showVerifyBookingDialog(e));
    }

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
}
