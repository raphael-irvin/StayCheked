package com.example.staycheked.dao;

import com.example.staycheked.Main;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Content;
import com.example.staycheked.model.object.Ticket;
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
        retrieveAllContents();
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
                    String rawContentMessage = content.getMessage();
                    // Escape commas in the message to avoid CSV format issues
                    String escapedMessage = rawContentMessage.replace(",", "$");

                    line = String.join(",",content.getTicketID(), content.getSender().getEmailAddress(), content.getDateTime().format(UtilService.dateTimeFormatter), escapedMessage);
                    Main.debug("ContentDAO", "Saving content: " + line); // Debugging output
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            Main.debug("ContentDAO", "Content Data Saving Failed");
            Main.debug("ContentDAO", "Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean retrieveAllContents() {

    //Refresh Ticket's Content Property to Avoid Duplicate Content
    for (Ticket ticket : DataStore.tickets.values()) {
        Main.debug("ContentDAO", "Clearing existing contents for ticket ID: " + ticket.getTicketID()); // Debugging output
        ticket.getContents().clear(); // Clear existing contents
    }

    HashMap<String, ArrayList<Content>> contents = new HashMap<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            Main.debug("ContentDAO", "Initializing Content Data..."); // Debugging output
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String ticketID = parts[0];
                    String userEmail = parts[1];
                    String lastUpdatedAtString = parts[2];

                    // Handle escaped commas in the message
                    String rawMessage = parts[3];
                    String message = rawMessage.replace("$", ","); // Replace escaped commas back to normal

                    User sender = DataStore.findUserByEmailAddress(userEmail);
                    LocalDateTime lastUpdatedAt = LocalDateTime.parse(lastUpdatedAtString, UtilService.dateTimeFormatter);

                    Content content = new Content(ticketID, sender, lastUpdatedAt, message);

                    if (!contents.containsKey(ticketID)) {
                        Main.debug("ContentDAO", "Creating new content list for ticketID: " + ticketID); // Debugging output
                        contents.put(ticketID, new ArrayList<>());
                        contents.get(ticketID).add(content);
                    } else {
                        Main.debug("ContentDAO", "Adding content to existing ticketID: " + ticketID); // Debugging output
                        contents.get(ticketID).add(content);
                    }

                    // For debugging purposes, print the guest details
                    Main.debug("ContentDAO", "Ticket ID: " + ticketID);
                    Main.debug("ContentDAO", "Sender Email: " + userEmail);
                    Main.debug("ContentDAO", "Last Updated At: " + lastUpdatedAt);
                    Main.debug("ContentDAO", "Message: " + message);
                }
            }
        } catch (IOException e) {
            Main.debug("ContentDAO", "Content Data Retrieval Failed");
        }

        DataStore.contents = contents;
        Main.debug("ContentDAO", "SAVED IN CONTENT DATASTORE: " + DataStore.contents); // Debugging output to check if contents are loaded correctly
        return true;
    }

}
