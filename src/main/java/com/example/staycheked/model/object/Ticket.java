package com.example.staycheked.model.object;

import com.example.staycheked.dao.TicketDAO;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ticket {

    //UID Tracker
    private static int ticketIDTracker = 0;

    //Properties
    private String ticketID;
    private Accommodation submittedTo;
    private Guest submittedBy;
    private Booking booking;
    private String subject;
    private String category;
    private ArrayList<Content> contents;
    private String status;
    private LocalDateTime lastUpdatedAt;


    //Constructor for creating new Ticket with all properties (Initialization)
    public Ticket(String ticketID, Booking booking, String subject, String category, String status, LocalDateTime lastUpdatedAt) {
        this.ticketID = ticketID;
        this.submittedTo = booking.getAccommodation();
        this.submittedBy = booking.getGuest();
        this.booking = booking;
        this.subject = subject;
        this.category = category;
        this.contents = new ArrayList<>();
        this.status = status;
        this.lastUpdatedAt = lastUpdatedAt;

        DataStore.tickets.put(this.ticketID, this);
    }

    //Constructor for creating new Ticket with minimal properties (RUNTIME)
    public Ticket(Accommodation submittedTo, Guest submittedBy, Booking booking, String subject, String category) {
        this.ticketID = generateTicketID();
        this.submittedTo = submittedTo;
        this.submittedBy = submittedBy;
        this.booking = booking;
        this.subject = subject;
        this.category = category;
        this.contents = new ArrayList<>();
        this.status = "Open"; // Default status
        this.lastUpdatedAt = LocalDateTime.now();

        DataStore.tickets.put(this.ticketID, this);
        TicketDAO.saveAllTickets(); // Save the new ticket to the database
        TicketDAO.retrieveAllTickets(); // Refresh the tickets from the database
    }

    public void addNewContent(Content content) {
        contents.add(content);
        lastUpdatedAt = content.getDateTime();
    }

    public String generateTicketID() {
        //Make sure the ticketIDTracker is set to the last ticketID in the database
        DataStore.tickets.values().stream()
                .mapToInt(ticket -> Integer.parseInt(ticket.getTicketID()))
                .max()
                .ifPresent(max -> ticketIDTracker = max);
        return String.valueOf(++ticketIDTracker);
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Content> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Content> contents) {
        this.contents = contents;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Guest getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Guest submittedBy) {
        this.submittedBy = submittedBy;
    }

    public Accommodation getSubmittedTo() {
        return submittedTo;
    }

    public void setSubmittedTo(Accommodation submittedTo) {
        this.submittedTo = submittedTo;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
