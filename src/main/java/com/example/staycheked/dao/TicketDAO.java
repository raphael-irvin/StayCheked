package com.example.staycheked.dao;

import com.example.staycheked.Main;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Ticket;
import com.example.staycheked.service.UtilService;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TicketDAO {

    private static final String DATA_SOURCE = "data/ticket.csv";


    //Tested as of 06/06/2025
    public static boolean initialize() {
        retrieveAllTickets();
        return true;
    }

    public static boolean saveAllTickets() {
        HashMap<String, Ticket> tickets = DataStore.tickets;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))) {
            String line = "ticketID,accommodationEmail,guestEmail,bookingID,subject,category,status,lastUpdatedAt";
            writer.write(line);
            writer.newLine();
            for (Ticket ticket : tickets.values()) {
                line = String.join(",", ticket.getTicketID(),
                        ticket.getSubmittedTo().getEmailAddress(),
                        ticket.getSubmittedBy().getEmailAddress(),
                        ticket.getBooking().getBookingID(),
                        ticket.getSubject(),
                        ticket.getCategory(),
                        ticket.getStatus(),
                        ticket.getLastUpdatedAt().format(UtilService.dateTimeFormatter));

                Main.debug("TicketDAO", "Saving ticket: " + line); // Debugging output

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            Main.debug("TicketDAO", "Ticket Data Saving Failed");
            return false;
        }
        return true;
    }

    public static boolean retrieveAllTickets() {
        HashMap<String, Ticket> tickets = new HashMap<>();
        Main.debug("TicketDAO", "Ticket Initialization Start Point"); // Debugging output

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String ticketID = parts[0];
                    // String accommodationEmail = parts[1]; -> Not used in this context
                    // String guestEmail = parts[2]; -> Not used in this context
                    String bookingID = parts[3]; 
                    String subject = parts[4];
                    String category = parts[5];
                    String status = parts[6];
                    String lastUpdatedAtString = parts[7];

                    LocalDateTime lastUpdatedAt = LocalDateTime.parse(lastUpdatedAtString, UtilService.dateTimeFormatter);

                    Ticket ticket = new Ticket(ticketID,
                            DataStore.findBookingByID(bookingID),
                            subject,
                            category,
                            status,
                            lastUpdatedAt);

                    tickets.put(ticketID, ticket);

                    // For debugging purposes, print the guest details
                    Main.debug("TicketDAO", ticketID);
                    Main.debug("TicketDAO", ticket.getSubmittedTo().getEmailAddress());
                    Main.debug("TicketDAO", ticket.getSubmittedBy().getEmailAddress());
                    Main.debug("TicketDAO", ticket.getBooking().getBookingID());
                    Main.debug("TicketDAO", ticket.getSubject());
                    Main.debug("TicketDAO", ticket.getCategory());
                    Main.debug("TicketDAO", ticket.getStatus());
                }
            }
        } catch (IOException e) {
            Main.debug("TicketDAO", "Ticket Data Retrieval Failed");
            ;
        }
        DataStore.tickets = tickets;
        return true;
    }

}
