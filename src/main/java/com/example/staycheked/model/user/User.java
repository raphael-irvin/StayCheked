package com.example.staycheked.model.user;

import com.example.staycheked.model.object.Ticket;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    //Properties
    private String userID;
    private String username;
    private String emailAddress;
    private String contactNo;
    private String password;

    //UserID Tracker
    private static int guestIDTracker = 0;
    private static int accommodationIDTracker = 0;
    private static int adminIDTracker = 0;


    public User(String username, String emailAddress, String contactNo, String password, String type) {
        this.username = username.toLowerCase();
        this.emailAddress = emailAddress.toLowerCase();
        this.contactNo = contactNo;
        this.password = password;
    }

    public static String generateUserID(String type) {
        return switch (type) {
            case "Accommodation" -> "a-" + (++accommodationIDTracker);
            case "Guest" -> "g-" + (++guestIDTracker);
            case "Admin" -> "op-" + (++adminIDTracker);
            default -> throw new IllegalArgumentException("Invalid user type: " + type);
        };
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
