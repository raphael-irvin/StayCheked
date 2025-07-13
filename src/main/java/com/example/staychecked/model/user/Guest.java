package com.example.staychecked.model.user;

import java.util.ArrayList;

import com.example.staychecked.model.DataStore;
import com.example.staychecked.model.object.Ticket;

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
        this.setUserID(User.generateUserID("Guest")); // Generate a new userID for the Guest

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
