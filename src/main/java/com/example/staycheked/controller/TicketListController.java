package com.example.staycheked.controller;

import java.util.HashMap;

import com.example.staycheked.Session;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.object.Ticket;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.service.TicketService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TicketListController {

    private TicketService ticketService;

    @FXML
    TableView<Ticket> activeTicketTable;
    @FXML
    TableColumn<Ticket, String> idColumn;
    @FXML
    TableColumn<Ticket, String> subjectColumn;
    @FXML
    TableColumn<Ticket, String> statusColumn;
    @FXML
    TableColumn<Ticket, String> dateColumn;
    @FXML
    TableColumn<Ticket, String> actionColumn;

    public void initialize() {
        HashMap<String, Ticket> matchingActiveTickets = new HashMap<>();

        if (Session.getCurrentUser() instanceof Guest) {
            //Find Tickets that belong to the current guest
            Guest currentGuest = (Guest) Session.getCurrentUser();
            for (Ticket ticket : DataStore.tickets.values()) {
                if (ticket.getSubmittedBy() != null && ticket.getSubmittedBy().getUserID().equals(currentGuest.getUserID())) {
                    matchingActiveTickets.put(ticket.getTicketID(), ticket);
                }
            }
        } else if (Session.getCurrentUser() instanceof Accommodation) {
            //Find Tickets that belong to the current accommodation
            Accommodation currentAccommodation = (Accommodation) Session.getCurrentUser();
            for (Ticket ticket : DataStore.tickets.values()) {
                if (ticket.getSubmittedTo() != null && ticket.getSubmittedTo().getUserID().equals(currentAccommodation.getUserID())) {
                    matchingActiveTickets.put(ticket.getTicketID(), ticket);
                }
            }
        } else {
            System.out.println("Unknown user type.");
        }

        // Initialize the table columns with appropriate cell value factories
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ticketID"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                com.example.staycheked.service.UtilService.getStringFormattedDateTime(
                    cellData.getValue().getLastUpdatedAt()
                )
            )
        );
        // Load active tickets into the table
        ObservableList<Ticket> tickets = FXCollections.observableArrayList(matchingActiveTickets.values());
        activeTicketTable.setItems(tickets);
    }

    public TicketListController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void onPostTicketButtonClick(String accommodationEmail, String guestEmail, Booking booking, String subject, String category, String contents) {
        if (ticketService.postNewTicket(accommodationEmail, guestEmail, booking, subject, category, contents) != null) {
            System.out.println("Ticket submitted successfully!");
            // Redirect to the main application view or show success message
        } else {
            System.out.println("Ticket submission failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void onReplyTicketButtonClick(String ticketID, String reply) {
        if (ticketService.replyTicket(DataStore.findTicketByID(ticketID), reply)) {
            System.out.println("Reply submitted successfully!");
            // Redirect to the main application view or show success message
        } else {
            System.out.println("Reply submission failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void onCloseTicketButtonClick(String ticketID) {
        ticketService.closeTicket(DataStore.findTicketByID(ticketID));
        System.out.println("Ticket closed successfully!");
        // Redirect to the main application view or show success message
    }

    public void onCancelTicketButtonClick(String ticketID) {
        ticketService.cancelTicket(DataStore.findTicketByID(ticketID));
        System.out.println("Ticket cancelled successfully!");
        // Redirect to the main application view or show success message
    }

}
