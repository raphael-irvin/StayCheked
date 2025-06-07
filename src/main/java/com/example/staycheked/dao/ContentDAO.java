package com.example.staycheked.dao;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Content;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.User;
import com.example.staycheked.service.UtilService;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class ContentDAO {

    private static final String DATA_SOURCE = "data/content.csv";

    //Tested as of 06/06/2025
    public static boolean initialize() {
        HashMap<String, ArrayList<Content>> contents = new HashMap<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            System.out.println("Initializing Content Data..."); // Debugging output
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String ticketID = parts[0];
                    String userEmail = parts[1];
                    String lastUpdatedAtString = parts[2];
                    String message = parts[3];

                    User sender = DataStore.findUserByEmailAddress(userEmail);
                    LocalDateTime lastUpdatedAt = LocalDateTime.parse(lastUpdatedAtString, UtilService.dateTimeFormatter);

                    Content content = new Content(ticketID, sender, lastUpdatedAt, message);

                    if (!contents.containsKey(ticketID)) {
                        System.out.println("Creating new content list for ticketID: " + ticketID); // Debugging output
                        contents.put(ticketID, new ArrayList<>());
                        contents.get(ticketID).add(content);
                    } else {
                        System.out.println("Adding content to existing ticketID: " + ticketID); // Debugging output
                        contents.get(ticketID).add(content);
                    }

                    // For debugging purposes, print the guest details
                    System.out.println("Ticket ID: " + ticketID);
                    System.out.println("Sender Email: " + userEmail);
                    System.out.println("Last Updated At: " + lastUpdatedAt);
                    System.out.println("Message: " + message);
                }
            }
        } catch (IOException e) {
            System.out.println("Content Data Initialization Failed");
        }
        DataStore.contents = contents;
        System.out.println("SAVED IN CONTENT DATASTORE: " + DataStore.contents); // Debugging output to check if contents are loaded correctly
        return true;
    }

    public static boolean saveAllContents() {
        HashMap<String, ArrayList<Content>> contents = DataStore.contents;
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))
        ) {
            String line = "ticketID,userEmail,lastUpdatedAt,message";
            writer.write(line);
            writer.newLine();

            for (String ticketID : contents.keySet()) {
                for (Content content : contents.get(ticketID)) {
                    line = String.join(",",content.getTicketID(), content.getSender().getEmailAddress(), content.getDateTime().format(UtilService.dateTimeFormatter), content.getMessage());
                    System.out.println("Saving content: " + line); // Debugging output
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Content Data Saving Failed");
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

}
