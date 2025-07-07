package com.example.staycheked.dao;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;

import java.io.*;
import java.util.HashMap;

public class BookingDAO {

    private static final String DATA_SOURCE = "data/booking.csv";

    //TESTED as of 06/06/2025
    public static boolean initialize() {
        retrieveAllBookings();
        return true;
    }

    public static boolean saveAllBookings() {
        HashMap<String, Booking> bookings = DataStore.bookings;
        System.out.println("ACCESSING DATASTORE: " + DataStore.bookings); // Debugging output to check if bookings are loaded correctly
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))
        ) {
            String line = "bookingID,accommodationEmailAddress,guestIdentificationLastName,room,status,guestEmailAddress";
            writer.write(line);
            writer.newLine();
            for (Booking booking : bookings.values()) {
                String guestEmail = booking.getGuest() != null ? booking.getGuest().getEmailAddress() : "null";
                line = String.join(",",
                        booking.getBookingID(),
                        booking.getAccommodation().getEmailAddress(),
                        booking.getGuestIdentificationLastName(),
                        booking.getRoom(),
                        booking.getStatus(),
                        guestEmail
                );
                System.out.println("Saving booking: " + line); // Debugging output
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to save bookings");
            return false;
        }
        return true;
    }

    public static boolean retrieveAllBookings() {
        HashMap<String, Booking> bookings = new HashMap<>();

        try (
                BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))
        ) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String bookingID = parts[0];
                    Accommodation accommodation = DataStore.findAccommodationByEmailAddress(parts[1]);
                    String guestIdentificationLastName = parts[2];
                    String room = parts[3];
                    String status = parts[4];

                    Guest guest = null;
                    if (!parts[5].equalsIgnoreCase("null")) {
                        guest = DataStore.findGuestByEmailAddress(parts[5]);
                    }

                    Booking booking = new Booking(bookingID, accommodation, guestIdentificationLastName, room, status, guest);
                    bookings.put(bookingID, booking);
                    // For debugging purposes, print the booking details
                    System.out.println("Saved Booking with Booking ID: " + bookingID);
                }
            }
        } catch (IOException e) {
            System.out.println("Booking Data Retrieval Failed");
            return false;
        }
        DataStore.bookings = bookings;
        return true;
    }

}
