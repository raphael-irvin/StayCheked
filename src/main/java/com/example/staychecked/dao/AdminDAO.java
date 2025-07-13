package com.example.staychecked.dao;

import java.io.*;
import java.util.HashMap;

import com.example.staychecked.Main;
import com.example.staychecked.model.DataStore;
import com.example.staychecked.model.user.Admin;

public class AdminDAO {

    private static final String DATA_SOURCE = "data/admin.csv";

    //TESTED as of 06/06/2025
    public static boolean initialize() {
        retrieveAllAdmins();
        return true;
    }

    public static boolean saveAllAdmins() {
        HashMap<String, Admin> admins = DataStore.admins;
        Main.debug("AdminDAO", "ACCESSING DATASTORE: " + DataStore.admins); // Debugging output to check if admins are loaded correctly
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))
        ) {
            String line = "userID,username,emailAddress,contactNo,password";
            writer.write(line);
            writer.newLine();
            for (Admin admin : admins.values()) {
                line = String.join(",", admin.getUserID(), admin.getUsername(), admin.getEmailAddress(),
                        admin.getContactNo(), admin.getPassword());

                Main.debug("AdminDAO", "Saving admin: " + line); // Debugging output

                writer.write(line);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            Main.debug("AdminDAO", "Admin Data Saving Failed");
            Main.debug("AdminDAO", e.getMessage());
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
                    Main.debug("AdminDAO", "Admin Email: " + emailAddress);
                    Main.debug("AdminDAO", "User ID: " + userID);
                    Main.debug("AdminDAO", "Username: " + username);
                    Main.debug("AdminDAO", "Contact No: " + contactNo);
                }
            }
        } catch (IOException e) {
            Main.debug("AdminDAO", "Admin Data Retrieval Failed");
            return false;
        }
        DataStore.admins = admins;
        Main.debug("AdminDAO", "SAVED IN DATASTORE: " + DataStore.admins); // Debugging output to check if admins are loaded correctly
        return true;
    }

}
