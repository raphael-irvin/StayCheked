package com.example.staycheked.model.user;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.object.Ticket;

import java.util.ArrayList;

public class Accommodation extends User{

    //Properties
    private String accommodationName;
    private String location;
    private Boolean status;

    private ArrayList<Booking> bookings = new ArrayList<>();
    private ArrayList<Ticket> receivedTickets = new ArrayList<>();


    // Constructor for creating new Accommodation with userID (Initialization)
    public Accommodation(String userID, String username, String emailAddress, String contactNo, String password, String accommodationName, String location, String status) {
        super(username, emailAddress, contactNo, password, "Accommodation");
        this.setUserID(userID);
        this.accommodationName = accommodationName;
        this.location = location;

        if (status.equalsIgnoreCase("verified")) {
            this.status = true;
        } else if (status.equalsIgnoreCase("unverified")) {
            this.status = false;
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        DataStore.accommodations.put(this.getEmailAddress(), this);
    }

    // Constructor for creating new Accommodation without a userID (RUNTIME)
    public Accommodation(String username, String emailAddress, String contactNo, String password, String accommodationName, String location) {
        super(username, emailAddress, contactNo, password, "Accommodation");
        this.accommodationName = accommodationName;
        this.location = location;
        this.status = false;

        DataStore.accommodations.put(this.getEmailAddress(), this);
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public ArrayList<Ticket> getReceivedTickets() {
        return receivedTickets;
    }

    public void setReceivedTickets(ArrayList<Ticket> receivedTickets) {
        this.receivedTickets = receivedTickets;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

}
