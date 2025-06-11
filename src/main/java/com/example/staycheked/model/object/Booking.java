package com.example.staycheked.model.object;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;

public class Booking {

    //These data should be pulled from the real database in a real application.
    private String bookingID;
    private Accommodation accommodation;
    private String guestIdentificationLastName;
    private String room;
    private String status;

    //These data will be updated upon successful booking verification.
    private Guest guest = null; // -> The user who verified the booking, doesn't have to be the same as the guest who made the booking.

    public Booking(String bookingID, Accommodation accommodation, String guestIdentificationLastName, String room, String status, Guest guest) {
        this.bookingID = bookingID;
        this.accommodation = accommodation;
        this.guestIdentificationLastName = guestIdentificationLastName;
        this.room = room;
        this.status = status; // Default status
        this.guest = guest;

        DataStore.bookings.put(this.bookingID, this);
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public String getGuestIdentificationLastName() {
        return guestIdentificationLastName;
    }

    public void setGuestIdentificationLastName(String guestIdentificationLastName) {
        this.guestIdentificationLastName = guestIdentificationLastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}
