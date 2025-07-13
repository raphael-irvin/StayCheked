package com.example.staychecked.service;

import java.util.ArrayList;

import com.example.staychecked.Session;
import com.example.staychecked.dao.BookingDAO;
import com.example.staychecked.model.DataStore;
import com.example.staychecked.model.object.Booking;
import com.example.staychecked.model.user.Guest;
import com.example.staychecked.model.user.User;

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

    //UTLITIY FUNCTION
    public static ArrayList<Booking> getVerifiedBookingsBasedOnLoggedUser(User user) {
        ArrayList<Booking> verifiedBookings = new ArrayList<>();
        for (Booking booking : DataStore.bookings.values()) {
            if (booking.getGuest() != null && booking.getGuest().getEmailAddress().equals(user.getEmailAddress())) {
                verifiedBookings.add(booking);
            }
        }
        return verifiedBookings;
    }

}