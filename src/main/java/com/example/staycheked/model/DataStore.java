package com.example.staycheked.model;

import com.example.staycheked.Main;
import com.example.staycheked.dao.*;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.object.Content;
import com.example.staycheked.model.object.FAQ;
import com.example.staycheked.model.object.Ticket;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Admin;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStore {

    /*
    These static ArrayList acts as the main database,
    every lookup and required data should be done in this central database.
    */

    public static HashMap<String, Booking> bookings = new HashMap<>();
    public static HashMap<String, Ticket> tickets = new HashMap<>();
    public static HashMap<String, ArrayList<Content>> contents = new HashMap<>();
    public static HashMap<String, Guest> guests = new HashMap<>();
    public static HashMap<String, Accommodation> accommodations = new HashMap<>();
    public static HashMap<String, Admin> admins = new HashMap<>();
    public static ArrayList<FAQ> faqs = new ArrayList<>();

    //Prevent Instantiation
    private DataStore() {}

    public static User findUserByEmailAddress(String emailAddress) {
        User user = findGuestByEmailAddress(emailAddress);
        if (user != null) return user;

        user = findAccommodationByEmailAddress(emailAddress);
        if (user != null) return user;

        user = findAdminByEmailAddress(emailAddress);
        return user; // Returns null if no user found
    }

    public static Guest findGuestByEmailAddress(String emailAddress) {
        for (Guest guest : guests.values()) {
            if (guest.getEmailAddress().equalsIgnoreCase(emailAddress)) {
                return guest;
            }
        }
        return null; // or throw an exception
    }

    public static Accommodation findAccommodationByEmailAddress(String emailAddress) {
        for (Accommodation accommodation : accommodations.values()) {
            if (accommodation.getEmailAddress().equalsIgnoreCase(emailAddress)) {
                return accommodation;
            }
        }
        return null; // or throw an exception
    }

    public static Admin findAdminByEmailAddress(String emailAddress) {
        for (Admin admin : admins.values()) {
            if (admin.getEmailAddress().equalsIgnoreCase(emailAddress)) {
                return admin;
            }
        }
        return null; // or throw an exception
    }

    public static Booking findBookingByID(String bookingID) {
        return bookings.get(bookingID);
    }

    public static Ticket findTicketByID(String ticketID) {
        return tickets.get(ticketID);
    }

    public static void dataInitialization() {
        Main.debug("DataStore", "Initializitation of DataStore started.");
        AccommodationDAO.initialize();
        Main.debug("DataStore", "AccommodationDAO initialized.");
        GuestDAO.initialize();
        Main.debug("DataStore", "GuestDAO initialized.");
        AdminDAO.initialize();
        Main.debug("DataStore", "AdminDAO initialized.");
        BookingDAO.initialize();
        Main.debug("DataStore", "BookingDAO initialized.");
        TicketDAO.initialize();
        Main.debug("DataStore", "TicketDAO initialized.");
        ContentDAO.initialize();
        Main.debug("DataStore", "ContentDAO initialized.");
    }

    public static void saveAllData() {
        GuestDAO.saveAllGuests();
        AccommodationDAO.saveAllAccommodations();
        AdminDAO.saveAllAdmins();
        BookingDAO.saveAllBookings();
        TicketDAO.saveAllTickets();
        ContentDAO.saveAllContents();
    }

}
