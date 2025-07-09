package com.example.staycheked.controller;

import com.example.staycheked.Main;
import com.example.staycheked.model.object.Content;
import com.example.staycheked.model.object.Ticket;
import com.example.staycheked.service.TicketService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    ListView<String> contentListView;
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
            sendResponseButton.setVisible(false);
            responseField.setVisible(false);
            closeTicketButton.setVisible(false);
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
        ticketIdLabel.setText(ticket.getTicketID());
        subjectLabel.setText(ticket.getSubject());
        lastUpdatedLabel.setText("Requested by " + ticket.getSubmittedBy().getUsername() + " - Last Updated at "+ ticket.getLastUpdatedAt().toString());

        contentListView.getItems().clear();
        Main.debug("TicketDetailController", "Populating content list view for ticket ID: " + ticket.getTicketID());
        Main.debug("TicketDetailController", "Content count: " + ticket.getContents().size());
        for (Content content : ticket.getContents()) {
            Main.debug("TicketDetailController", "Adding content to list view: " + content.getMessage());
            contentListView.getItems().add(
                content.getDateTime() + " | " + content.getSender().getUsername() + ": " + content.getMessage()
            );
        }
    }

}
