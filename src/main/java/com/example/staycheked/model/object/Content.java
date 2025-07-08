package com.example.staycheked.model.object;

import com.example.staycheked.Main;
import com.example.staycheked.dao.ContentDAO;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Content {

    //Properties
    private String ticketID;
    private User sender;
    private LocalDateTime dateTime;
    private String message;

    // Constructor for creating new Content with all properties (Initialization)
    public Content(String ticketID, User sender, LocalDateTime dateTime, String message) {
        this.ticketID = ticketID;
        this.sender = sender;
        this.dateTime = dateTime;
        this.message = message;

        Main.debug("Content", "Creating new content for ticket ID: " + ticketID);
        Main.debug("Content", "Does Ticket Exist in DataStore: " + DataStore.tickets.get(ticketID));
        DataStore.tickets.get(ticketID).addNewContent(this);
        Main.debug("Content", "Successfully Inserted to Ticket Object, Contents in Ticket now: " + DataStore.tickets.get(ticketID).getContents().size());
    }

    // Constructor for creating new Content with minimal properties (RUNTIME)
    public Content(String ticketID, User sender, String message) {
        this.ticketID = ticketID;
        this.sender = sender;
        this.message = message;
        this.dateTime = LocalDateTime.now();
        Main.debug("Content", "Creating new runtime content for ticket ID: " + ticketID);
        DataStore.contents.computeIfAbsent(ticketID, _ -> new ArrayList<>()).add(this); // Add content to the DataStore
        Main.debug("Content", "Added content to DataStore.contents for ticket ID: " + ticketID + ", total contents: " + DataStore.contents.get(ticketID).size());
        ContentDAO.saveAllContents(); // Save the new content to the database
        Main.debug("Content", "Saved all contents to database for ticket ID: " + ticketID);
        ContentDAO.retrieveAllContents(); // Refresh the contents from the database
        Main.debug("Content", "Retrieved all contents from database for ticket ID: " + ticketID);
    }

    public Ticket getTicket() {
        return DataStore.tickets.get(ticketID);
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
