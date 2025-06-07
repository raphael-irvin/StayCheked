package com.example.staycheked.controller;

import com.example.staycheked.service.BookingAuthService;

public class BookingAuthController {

    private BookingAuthService bookingAuthService;

    public void setBookingAuthService(BookingAuthService bookingAuthService) {
        this.bookingAuthService = bookingAuthService;
    }

    public void onVerifyBookingButtonClick(String bookingID, String guestIdentificationLastName) {
        if (bookingAuthService.verifyBooking(bookingID, guestIdentificationLastName) != null) {
            System.out.println("Booking verification successful!");
            // Redirect to the main application view or show success message
        } else {
            System.out.println("Booking verification failed. Please check your details.");
            // Show error message in the UI
        }
    }

}
