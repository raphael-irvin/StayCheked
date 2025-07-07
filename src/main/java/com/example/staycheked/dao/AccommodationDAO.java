package com.example.staycheked.dao;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Accommodation;

import java.io.*;
import java.util.HashMap;

public class AccommodationDAO {

    private static final String DATA_SOURCE = "data/accommodation.csv";

    //TESTED as of 06/06/2025
    public static boolean initialize() {
        retrieveAllAccommodations();
        return true;
    }

    public static boolean saveAllAccommodations() {
        HashMap<String, Accommodation> accommodations = DataStore.accommodations;
        System.out.println("ACCESSING DATASTORE: " + DataStore.accommodations); // Debugging output to check if accommodations are loaded correctly
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))
        ) {
            String line = "userID,username,emailAddress,contactNo,password,accommodationName,address,status";
            writer.write(line);
            writer.newLine();

            for (Accommodation accommodation : accommodations.values()) {
                line = String.join(",", accommodation.getUserID(), accommodation.getUsername(), accommodation.getEmailAddress(),
                        accommodation.getContactNo(), accommodation.getPassword(), accommodation.getAccommodationName(),
                        accommodation.getLocation(), accommodation.getStatus() ? "verified" : "unverified");

                System.out.println("Saving accommodation: " + line); // Debugging output

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Accommodation Data Saving Failed");
            return false;
        }
        return true;
    }

    public static boolean retrieveAllAccommodations() {
HashMap<String, Accommodation> accommodations = new HashMap<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String userID = parts[0];
                    String username = parts[1];
                    String emailAddress = parts[2];
                    String contactNo = parts[3];
                    String password = parts[4];
                    String accommodationName = parts[5];
                    String address = parts[6];
                    String status = parts[7];
                    Accommodation accommodation = new Accommodation(userID, username, emailAddress, contactNo, password, accommodationName, address, status);
                    accommodations.put(emailAddress, accommodation);

                    // For debugging purposes, print the guest details
                    System.out.println(emailAddress);
                    System.out.println(accommodation.getUserID());
                    System.out.println(accommodation.getUsername());
                    System.out.println(accommodation.getEmailAddress());
                    System.out.println(accommodation.getContactNo());
                    System.out.println(accommodation.getPassword());
                    System.out.println(accommodation.getAccommodationName());
                    System.out.println(accommodation.getLocation());
                    System.out.println(accommodation.getStatus());
                }
            }
        } catch (IOException e) {
            System.out.println("Accommodation Data Retrieval Failed");
            return false;
        }
        DataStore.accommodations = accommodations;
        System.out.println("SAVED IN DATASTORE: " + DataStore.accommodations); // Debugging output to check if accommodations are loaded correctly
        return true;
    }

}
