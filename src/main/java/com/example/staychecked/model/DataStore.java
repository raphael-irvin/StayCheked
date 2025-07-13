package com.example.staychecked.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.staychecked.Main;
import com.example.staychecked.dao.*;
import com.example.staychecked.model.object.Booking;
import com.example.staychecked.model.object.Content;
import com.example.staychecked.model.object.FAQ;
import com.example.staychecked.model.object.Ticket;
import com.example.staychecked.model.user.Accommodation;
import com.example.staychecked.model.user.Admin;
import com.example.staychecked.model.user.Guest;
import com.example.staychecked.model.user.User;
import com.example.staychecked.service.UtilService;

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
        if (user != null) return user;

        return null; // Returns null if no user found
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
        FAQDAO.initialize();
        Main.debug("DataStore", "FAQDAO initialized.");
        refreshRAGDocument();
    }

    public static void saveAllData() {
        GuestDAO.saveAllGuests();
        AccommodationDAO.saveAllAccommodations();
        AdminDAO.saveAllAdmins();
        BookingDAO.saveAllBookings();
        TicketDAO.saveAllTickets();
        ContentDAO.saveAllContents();
        FAQDAO.saveAllFAQs();
    }

    public static void refreshData() {
        Main.debug("DataStore", "Refreshing DataStore.");
        saveAllData();
        // Reinitialize the data store to reload all data
        bookings.clear();
        tickets.clear();
        contents.clear();
        guests.clear();
        accommodations.clear();
        admins.clear();
        faqs.clear();
        Main.debug("DataStore", "DataStore cleared, reinitializing data.");
        dataInitialization();
        refreshRAGDocument();
    }

    public static void refreshRAGDocument() {
        Main.debug("DataStore", "Refreshing RAG Document.");
        // Logic to refresh the RAG Document
        try (
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/RAGDocument/main.txt"));
        ) {
            writer.write("RAG Document refreshed at: " + LocalDateTime.now().format(UtilService.dateTimeFormatter));
            writer.newLine();
            writer.write("This document contains the latest information from the staychecked system.");
            writer.newLine();
            writer.write("The current total number of bookings registered in the system: " + bookings.size());
            writer.newLine();
            writer.write("The current total number of tickets registered in the system: " + tickets.size());
            writer.newLine();
            writer.write("The current total number of guests registered in the system: " + guests.size());
            writer.newLine();
            writer.write("The current total number of accommodations registered in the system: " + accommodations.size());

            try (
                BufferedReader reader = new BufferedReader(new FileReader("data/RAGDocument/appDesc.txt"))
            ) {
                Main.debug("DataStore", "Reading application description from appDesc.txt");
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (Exception e) {
            Main.debug("DataStore", "Error refreshing RAG Document: " + e.getMessage());
            return;
        }

        Main.debug("DataStore", "RAG Document refreshed successfully.");
    }

}
