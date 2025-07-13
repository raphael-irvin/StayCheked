package com.example.staychecked.dao;

import java.io.*;
import java.util.HashMap;

import com.example.staychecked.Main;
import com.example.staychecked.model.DataStore;
import com.example.staychecked.model.user.Guest;

//TESTED as of 06/06/2025

public class GuestDAO {

    private static final String DATA_SOURCE = "data/guest.csv";

    //TESTED as of 06/06/2025
    public static boolean initialize() {
        retrieveAllGuests();
        return true;
    }

    public static boolean saveAllGuests() {
        HashMap<String, Guest> guests = DataStore.guests;
        Main.debug("GuestDAO", "ACCESSING DATASTORE: " + DataStore.guests); // Debugging output to check if guests are loaded correctly
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))
                ) {
            String line = "userID,username,emailAddress,contactNo,password,fullName";
            writer.write(line);
            writer.newLine();
            for (Guest guest : guests.values()) {
                line = String.join(",", guest.getUserID(), guest.getUsername(), guest.getEmailAddress(),
                        guest.getContactNo(), guest.getPassword(), guest.getFullName());

                Main.debug("GuestDAO", "Saving guest: " + line);

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            Main.debug("GuestDAO", "Failed to save guest data: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean retrieveAllGuests() {
        HashMap<String, Guest> guests = new HashMap<>();
        Main.debug("GuestDAO", "Guest Initialization Start Point"); // Debugging output

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String userID = parts[0];
                    String username = parts[1];
                    String emailAddress = parts[2];
                    String contactNo = parts[3];
                    String password = parts[4];
                    String fullName = parts[5];
                    Guest guest = new Guest(userID, username, emailAddress, contactNo, password, fullName);
                    guests.put(emailAddress, guest);

                    // For debugging purposes, print the guest details
                    Main.debug("GuestDAO", "Retrieved guest: " + guest.getUsername() + ", Email: " + guest.getEmailAddress());
                    if (userID != null && contactNo != null && password != null && fullName != null) {
                        Main.debug("GuestDAO", "Guest ID: " + userID + ", Contact No: " + contactNo + ", Full Name: " + fullName);
                    } else {
                        Main.debug("GuestDAO", "Incomplete guest data for email: " + emailAddress);
                    }
                }
            }
        } catch (IOException e) {
            Main.debug("GuestDAO", "Guest Data Retrieval Failed");
            ;
        }
        DataStore.guests = guests;
        // For debugging purposes, print the size of the guests HashMap
        for (Guest guest : DataStore.guests.values()) {
            Main.debug("GuestDAO", "REGISTERED GUEST: " + guest.getUserID());
            Main.debug("GuestDAO", "Guest Email: " + guest.getEmailAddress());
            Main.debug("GuestDAO", "User ID: " + guest.getUserID());
            Main.debug("GuestDAO", "Username: " + guest.getUsername());
            Main.debug("GuestDAO", "Contact No: " + guest.getContactNo());
        }   
        Main.debug("GuestDAO", "Guest Keys: " + DataStore.guests.keySet());
        return true;
    }

}
