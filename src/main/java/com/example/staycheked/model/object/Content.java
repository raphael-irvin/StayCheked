package com.example.staycheked.model.object;

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

    // Constructor for creating new Content with all properties (Initialization)`
    public Content(String ticketID, User sender, LocalDateTime dateTime, String message) {
        this.ticketID = ticketID;
        this.sender = sender;
        this.dateTime = dateTime;
        this.message = message;

        System.out.println("DEBUG - Content: Creating new content for ticket ID: " + ticketID); //DEBUGGING OUTPUT
        System.out.println("DEBUG - Content: Does Ticket Exist in DataStore: " + DataStore.tickets.get(ticketID)); //DEBUGGING OUTPUT
        DataStore.tickets.get(ticketID).addNewContent(this);
        System.out.println("DEBUG - Content: Sucessfully Inserted to Ticket Object, Contents in Ticket now: " + DataStore.tickets.get(ticketID).getContents().size()); //DEBUGGING OUTPUT
    }

    // Constructor for creating new Content with minimal properties (RUNTIME)
    public Content(String ticketID, User sender, String message) {
        this.ticketID = ticketID;
        this.sender = sender;
        this.message = message;
        this.dateTime = LocalDateTime.now();
        DataStore.contents.computeIfAbsent(ticketID, _ -> new ArrayList<>()).add(this); // Add content to the DataStore
        ContentDAO.saveAllContents(); // Save the new content to the database
        ContentDAO.retrieveAllContents(); // Refresh the contents from the database
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
