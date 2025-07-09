package com.example.staycheked.service;

import com.example.staycheked.Session;
import com.example.staycheked.dao.ContentDAO;
import com.example.staycheked.dao.TicketDAO;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.object.Content;
import com.example.staycheked.model.object.Ticket;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;

import java.time.LocalDateTime;

public class TicketService {

    public Ticket postNewTicket(String accommodationEmail, String guestEmail, Booking booking, String subject, String category, String contents) {
        Guest sender = DataStore.findGuestByEmailAddress(guestEmail);
        Accommodation recipient = DataStore.findAccommodationByEmailAddress(accommodationEmail);

        Ticket newTicket = new Ticket(recipient, sender, booking, subject, category);
        newTicket.addNewContent(new Content(newTicket.getTicketID(), sender, contents));

        newTicket.getSubmittedTo().getReceivedTickets().add(newTicket);
        newTicket.getSubmittedBy().getSubmittedTickets().add(newTicket);

        return newTicket;
    }

    public boolean replyTicket(Ticket ticket, String reply) {
        Content content = new Content(ticket.getTicketID(), Session.getCurrentUser(), reply);
        ticket.setLastUpdatedAt(content.getDateTime());
        return true;
    }

    public void closeTicket(Ticket ticket) {
        ticket.setStatus("Closed");
        ticket.setLastUpdatedAt(LocalDateTime.now());

        //Content needs to be saved because it is dependent with the ticket Object
        ContentDAO.saveAllContents();
        TicketDAO.saveAllTickets();
        TicketDAO.retrieveAllTickets();
        ContentDAO.retrieveAllContents();
    }

    public void cancelTicket(Ticket ticket) {
        ticket.getSubmittedTo().getReceivedTickets().remove(ticket);
        ticket.getSubmittedBy().getSubmittedTickets().remove(ticket);
    }

}
