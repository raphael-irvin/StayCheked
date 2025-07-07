package com.example.staycheked.service;

import com.example.staycheked.Session;
import com.example.staycheked.dao.BookingDAO;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.user.Guest;

public class BookingAuthService {

    public Booking verifyBooking(String bookingID, String guestIdentificationLastName) {
        if (DataStore.findBookingByID(bookingID) != null) {
            Booking booking = DataStore.findBookingByID(bookingID);
            if (booking.getGuestIdentificationLastName().equalsIgnoreCase(guestIdentificationLastName)) {
                booking.setGuest((Guest) Session.getCurrentUser());
                booking.setStatus("Verified");
                BookingDAO.saveAllBookings(); // Save the updated booking status to the database
                BookingDAO.retrieveAllBookings(); // Refresh the bookings from the database
                return booking;
            }
        }

        return null;
    }

}