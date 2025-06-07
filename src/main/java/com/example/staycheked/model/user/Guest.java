package com.example.staycheked.model.user;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Ticket;

import java.util.ArrayList;

public class Guest extends User {

    //Properties
    private String fullName;

    ArrayList<Ticket> submittedTickets = new ArrayList<>();

    // Constructor for creating new Guest with userID (Initialization)
    public Guest(String userID, String username, String emailAddress, String contactNo, String password, String fullName) {
        super(username, emailAddress, contactNo, password, "Guest");
        this.setUserID(userID);
        this.fullName = fullName;

        DataStore.guests.put(this.getEmailAddress(), this);
    }

    // Constructor for creating new Guest without a userID (RUNTIME)
    public Guest(String username, String emailAddress, String contactNo, String password, String fullName) {
        super(username, emailAddress, contactNo, password, "Guest");
        this.fullName = fullName;

        DataStore.guests.put(this.getEmailAddress(), this);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ArrayList<Ticket> getSubmittedTickets() {
        return submittedTickets;
    }

    public void setSubmittedTickets(ArrayList<Ticket> submittedTickets) {
        this.submittedTickets = submittedTickets;
    }

}
