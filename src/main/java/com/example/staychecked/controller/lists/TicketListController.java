package com.example.staychecked.controller.lists;

import java.io.IOException;
import java.util.HashMap;

import com.example.staychecked.Main;
import com.example.staychecked.Session;
import com.example.staychecked.controller.TicketDetailController;
import com.example.staychecked.model.DataStore;
import com.example.staychecked.model.object.Ticket;
import com.example.staychecked.model.user.Accommodation;
import com.example.staychecked.model.user.Guest;
import com.example.staychecked.service.TicketService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
                if (ticket.getSubmittedTo() != null && ticket.getSubmittedTo() == currentAccommodation) {
                    matchingActiveTickets.put(ticket.getTicketID(), ticket);
                }
            }
        } else {
            Main.debug("TicketListController", "Unknown user type.");
        }

        // Initialize the table columns with appropriate cell value factories
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ticketID"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                com.example.staychecked.service.UtilService.getStringFormattedDateTime(
                    cellData.getValue().getLastUpdatedAt()
                )
            )
        );
        // Load active tickets into the table
        ObservableList<Ticket> tickets = FXCollections.observableArrayList(matchingActiveTickets.values());
        activeTicketTable.setItems(tickets);

        //Setup Row Factory for double-click action
        activeTicketTable.setRowFactory(_ -> {
            javafx.scene.control.TableRow<Ticket> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Ticket selectedTicket = row.getItem();
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/staychecked/views/ContentViews/DetailViews/GuestTicketDetailView.fxml"));
                        fxmlLoader.setController(new TicketDetailController(new TicketService(), selectedTicket));
                        Parent root = fxmlLoader.load();

                        // Find the BorderPane (assumes the table is inside a BorderPane center)
                        javafx.scene.Node node = activeTicketTable;
                        while (node != null && !(node instanceof javafx.scene.layout.BorderPane)) {
                            node = node.getParent();
                        }
                        if (node instanceof javafx.scene.layout.BorderPane borderPane) {
                            borderPane.setCenter(root);
                        } else {
                            Main.debug("TicketListController", "Could not find parent BorderPane to set center.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

    public TicketListController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void onReplyTicketButtonClick(String ticketID, String reply) {
        if (ticketService.replyTicket(DataStore.findTicketByID(ticketID), reply)) {
            Main.debug("TicketListController", "Reply submitted successfully!");
            // Redirect to the main application view or show success message
        } else {
            Main.debug("TicketListController", "Reply submission failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void onCloseTicketButtonClick(String ticketID) {
        ticketService.closeTicket(DataStore.findTicketByID(ticketID));
        Main.debug("TicketListController", "Ticket closed successfully!");
        // Redirect to the main application view or show success message
    }

    public void onCancelTicketButtonClick(String ticketID) {
        ticketService.cancelTicket(DataStore.findTicketByID(ticketID));
        Main.debug("TicketListController", "Ticket cancelled successfully!");
        // Redirect to the main application view or show success message
    }

}
