package com.example.staycheked.controller;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.service.TicketService;

public class TicketController {

    private TicketService ticketService;

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public void onPostTicketButtonClick(String accommodationEmail, String guestEmail, Booking booking, String subject, String category, String contents) {
        if (ticketService.postNewTicket(accommodationEmail, guestEmail, booking, subject, category, contents) != null) {
            System.out.println("Ticket submitted successfully!");
            // Redirect to the main application view or show success message
        } else {
            System.out.println("Ticket submission failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void onReplyTicketButtonClick(String ticketID, String reply) {
        if (ticketService.replyTicket(DataStore.findTicketByID(ticketID), reply)) {
            System.out.println("Reply submitted successfully!");
            // Redirect to the main application view or show success message
        } else {
            System.out.println("Reply submission failed. Please check your details.");
            // Show error message in the UI
        }
    }

    public void onCloseTicketButtonClick(String ticketID) {
        ticketService.closeTicket(DataStore.findTicketByID(ticketID));
        System.out.println("Ticket closed successfully!");
        // Redirect to the main application view or show success message
    }

    public void onCancelTicketButtonClick(String ticketID) {
        ticketService.cancelTicket(DataStore.findTicketByID(ticketID));
        System.out.println("Ticket cancelled successfully!");
        // Redirect to the main application view or show success message
    }

}
