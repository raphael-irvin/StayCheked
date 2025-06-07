package com.example.staycheked.dao;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Guest;

import java.io.*;
import java.util.HashMap;

//TESTED as of 06/06/2025

public class GuestDAO {

    private static final String DATA_SOURCE = "data/guest.csv";

    //TESTED as of 06/06/2025
    public static boolean initialize() {
        HashMap<String, Guest> guests = new HashMap<>();

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
                    System.out.println(emailAddress);
                    System.out.println(guest.getUserID());
                    System.out.println(guest.getUsername());
                    System.out.println(guest.getEmailAddress());
                    System.out.println(guest.getContactNo());
                    System.out.println(guest.getPassword());
                    System.out.println(guest.getFullName());
                }
            }
        } catch (IOException e) {
            System.out.println("Guest Data Initialization Failed");
            ;
        }
        DataStore.guests = guests;
        return true;
    }

    public static boolean saveAllGuests() {
        HashMap<String, Guest> guests = DataStore.guests;
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))
                ) {
            String line = "userID,username,emailAddress,contactNo,password,fullName";
            writer.write(line);
            writer.newLine();
            for (Guest guest : guests.values()) {
                line = String.join(",", guest.getUserID(), guest.getUsername(), guest.getEmailAddress(),
                        guest.getContactNo(), guest.getPassword(), guest.getFullName());

                System.out.println("Saving guest: " + line); // Debugging output

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save guest data: " + e.getMessage());
            return false;
        }
        return true;
    }

}
