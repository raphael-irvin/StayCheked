package com.example.staychecked.controller;

import com.example.staychecked.Session;
import com.example.staychecked.model.object.Content;
import com.example.staychecked.model.object.Ticket;
import com.example.staychecked.service.TicketService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TicketDetailController {

    private TicketService ticketService;
    private Ticket ticket;

    @FXML
    Label ticketIdLabel;
    @FXML
    Label subjectLabel;
    @FXML
    Label lastUpdatedLabel;
    @FXML
    VBox messageContainer; // Changed from ListView to VBox for better layout control
    @FXML
    TextField responseField;
    @FXML
    Button sendResponseButton;
    @FXML
    Button closeTicketButton;

    //Initialization
    public void initialize() {
        //All Initialization is done through refreshView() for future proofing
        refreshView();

        // Set up the UI components and event handlers
        sendResponseButton.setOnAction(_ -> onPostNewReplyButtonClick());
        closeTicketButton.setOnAction(_ -> onCloseTicketButtonClick());

        //Set up conditional button visibility based conditions
        if (ticket.getStatus().equals("Closed")) {
            sendResponseButton.setDisable(true);
            responseField.setDisable(true);
            closeTicketButton.setDisable(true);
            lastUpdatedLabel.setText("Ticket is closed at " + ticket.getLastUpdatedAt().toString() + ". No further replies can be sent.");
        } else {
            sendResponseButton.setDisable(false);
            responseField.setDisable(false);
        }

    }

    public TicketDetailController(TicketService ticketService, Ticket ticket) {
        this.ticketService = ticketService;
        this.ticket = ticket;
    }

    public void onPostNewReplyButtonClick() {
        String response = responseField.getText();
        if (response.isEmpty()) {
            System.out.println("DEBUG - TicketDetailController: Response field is empty, not sending reply.");
            // Show Alert Dialog to User
            Dialog<String> alertDialog = new Dialog<>();
            alertDialog.setTitle("Empty Response");
            alertDialog.setContentText("Please enter a response before sending.");
            alertDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            alertDialog.showAndWait();
            return;
        }

        ticketService.replyTicket(ticket, response);

        // Clear the response field after sending
        responseField.clear();

        // Refresh the content list view to show the new reply
        refreshView();
    }

    public void onCloseTicketButtonClick() {
        //Confirm with the user before closing the ticket
        Dialog<ButtonType> confirmationDialog = new Dialog<>();
        confirmationDialog.setTitle("Close Ticket Confirmation");
        confirmationDialog.setContentText("Are you sure you want to close this ticket? Closed tickets cannot be reopened.");
        confirmationDialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        ButtonType result = confirmationDialog.showAndWait().orElse(ButtonType.NO);

        if (result != ButtonType.YES) {
            // User chose not to close the ticket
            return;
        }

        // Close the ticket
        ticketService.closeTicket(ticket);

        // Refresh the view to reflect the closed status
        refreshView();

        // Disable the response field and button since the ticket is closed
        responseField.setDisable(true);
        sendResponseButton.setDisable(true);
        closeTicketButton.setDisable(true);
    }

    public void refreshView() {
        // Populate the UI with ticket details
        ticketIdLabel.setText("Ticket ID: " + ticket.getTicketID());
        subjectLabel.setText(ticket.getSubject());
        lastUpdatedLabel.setText("Requested by " + ticket.getSubmittedBy().getUsername() + " - Last Updated at "+ ticket.getLastUpdatedAt().toString());

        messageContainer.getChildren().clear(); // Clear previous messages
        for (Content content : ticket.getContents()) {

            VBox bubbleContainer = new VBox();
            bubbleContainer.setSpacing(5);

            Label senderLabel = new Label(content.getSender().getUsername() + ": ");
            senderLabel.getStyleClass().add("message-info");
            senderLabel.getStyleClass().add("message-info-sender");
            Label timeStampLabel = new Label(content.getDateTime().toString() + " - ");
            timeStampLabel.getStyleClass().add("message-info");
            timeStampLabel.getStyleClass().add("message-info-timestamp");

            VBox messageInfoContainer = new VBox();
            messageInfoContainer.getChildren().addAll(timeStampLabel, senderLabel);
            bubbleContainer.getChildren().add(messageInfoContainer);

            Label messageLabel = new Label(content.getMessage());
            messageLabel.setPrefWidth(Control.USE_COMPUTED_SIZE);
            messageLabel.getStyleClass().add("message-bubble");

            if (content.getSender() == Session.getCurrentUser()) {
                messageLabel.getStyleClass().add("message-bubble-sent");
            } else {
                messageLabel.getStyleClass().add("message-bubble-received");
            }

            bubbleContainer.getChildren().add(messageLabel);
            messageContainer.getChildren().add(bubbleContainer);
        }
    }
}
