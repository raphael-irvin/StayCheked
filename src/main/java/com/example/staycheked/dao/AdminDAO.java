package com.example.staycheked.dao;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Admin;

import java.io.*;
import java.util.HashMap;

public class AdminDAO {

    private static final String DATA_SOURCE = "data/admin.csv";

    //TESTED as of 06/06/2025
    public static boolean initialize() {
        retrieveAllAdmins();
        return true;
    }

    public static boolean saveAllAdmins() {
        HashMap<String, Admin> admins = DataStore.admins;
        System.out.println("ACCESSING DATASTORE: " + DataStore.admins); // Debugging output to check if admins are loaded correctly
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))
        ) {
            String line = "userID,username,emailAddress,contactNo,password";
            writer.write(line);
            writer.newLine();
            for (Admin admin : admins.values()) {
                line = String.join(",", admin.getUserID(), admin.getUsername(), admin.getEmailAddress(),
                        admin.getContactNo(), admin.getPassword());

                System.out.println("Saving admin: " + line); // Debugging output

                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Admin Data Saving Failed");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean retrieveAllAdmins() {
        HashMap<String, Admin> admins = new HashMap<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String userID = parts[0];
                    String username = parts[1];
                    String emailAddress = parts[2];
                    String contactNo = parts[3];
                    String password = parts[4];

                    Admin admin = new Admin(userID, username, emailAddress, contactNo, password);
                    admins.put(emailAddress, admin);

                    // For debugging purposes, print the admin details
                    System.out.println("Admin Email: " + emailAddress);
                    System.out.println("User ID: " + userID);
                    System.out.println("Username: " + username);
                    System.out.println("Contact No: " + contactNo);
                }
            }
        } catch (IOException e) {
            System.out.println("Admin Data Retrieval Failed");
            return false;
        }
        DataStore.admins = admins;
        System.out.println("SAVED IN DATASTORE: " + DataStore.admins); // Debugging output to check if admins are loaded correctly
        return true;
    }

}
