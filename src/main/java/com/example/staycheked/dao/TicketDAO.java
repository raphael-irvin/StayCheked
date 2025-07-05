package com.example.staycheked.dao;

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
        HashMap<String, Ticket> tickets = new HashMap<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String ticketID = parts[0];
                    String accommodationEmail = parts[1];
                    String guestEmail = parts[2];
                    String bookingID = parts[3];
                    String subject = parts[4];
                    String category = parts[5];
                    String status = parts[6];
                    String lastUpdatedAtString = parts[7];

                    LocalDateTime lastUpdatedAt = LocalDateTime.parse(lastUpdatedAtString, UtilService.dateTimeFormatter);

                    Ticket ticket = new Ticket(ticketID,
                            DataStore.findAccommodationByEmailAddress(accommodationEmail),
                            DataStore.findGuestByEmailAddress(guestEmail),
                            DataStore.findBookingByID(bookingID),
                            subject,
                            category,
                            status,
                            lastUpdatedAt);

                    tickets.put(ticketID, ticket);

                    // For debugging purposes, print the guest details
                    System.out.println(ticketID);
                    System.out.println(ticket.getSubmittedTo().getEmailAddress());
                    System.out.println(ticket.getSubmittedBy().getEmailAddress());
                    System.out.println(ticket.getBooking().getBookingID());
                    System.out.println(ticket.getSubject());
                    System.out.println(ticket.getCategory());
                    System.out.println(ticket.getStatus());
                }
            }
        } catch (IOException e) {
            System.out.println("Guest Data Initialization Failed");
            ;
        }
        DataStore.tickets = tickets;
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

                System.out.println("Saving ticket: " + line); // Debugging output

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ticket Data Saving Failed");
            return false;
        }
        return true;
    }

}
