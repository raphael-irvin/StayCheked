package com.example.staycheked.controller;

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

    //Initialization
    public void initialize() {
        // Set up the UI components and event handlers
        sendResponseButton.setOnAction(_ -> onPostNewReplyButtonClick());

        //All Initialization is done through refreshView() for future proofing
        refreshView();
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

    public void refreshView() {
        // Populate the UI with ticket details
        ticketIdLabel.setText(ticket.getTicketID());
        subjectLabel.setText(ticket.getSubject());
        lastUpdatedLabel.setText("Requested by " + ticket.getSubmittedBy().getUsername() + " - Last Updated at "+ ticket.getLastUpdatedAt().toString());

        contentListView.getItems().clear();
        System.out.println("DEBUG - TicketDetailController: Populating content list view for ticket ID: " + ticket.getTicketID());
        System.out.println("DEBUG - TicketDetailController: Content count: " + ticket.getContents().size());
        for (Content content : ticket.getContents()) {
            System.out.println("DEBUG - TicketDetailController: Adding content to list view: " + content.getMessage());
            contentListView.getItems().add(
                content.getDateTime() + " | " + content.getSender().getUsername() + ": " + content.getMessage()
            );
        }
    }

}
