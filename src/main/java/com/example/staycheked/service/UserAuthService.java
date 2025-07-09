package com.example.staycheked.service;

import com.example.staycheked.Main;
import com.example.staycheked.dao.AccommodationDAO;
import com.example.staycheked.dao.GuestDAO;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import com.example.staycheked.model.user.User;

public class UserAuthService {

    public User login(String email, String password) {
        User user = DataStore.findUserByEmailAddress(email);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        }
        return null;
    }

    public Guest registerGuest(String username, String emailAddress, String contactNo, String password, String fullName) {
        if (checkAccountRedundancy(username, emailAddress, contactNo, "Guest")) {
            //Handle redundancy case, e.g., throw an exception or return null
            return null;
        }
        Guest guest = new Guest(username, emailAddress, contactNo, password, fullName);
        Main.debug("UserAuthService", "Put into Guest DataStore: " + guest.getUserID()); // Debugging output
        GuestDAO.saveAllGuests(); // Save the new guest to the data store
        Main.debug("UserAuthService", "Saved All Guest DataStore to CSV"); // Debugging output
        GuestDAO.retrieveAllGuests(); // Ensure the data store is updated
        Main.debug("UserAuthService", "Retrieved All Guest DataStore from CSV"); // Debugging output
        Main.debug("UserAuthService", "Guest registered: " + guest.getUsername()); // Debugging output
        Main.debug("UserAuthService", "isGuest registered: " + DataStore.guests.get(emailAddress)); // Debugging output
        return DataStore.guests.get(emailAddress);
    }

    public Accommodation registerAccommodation(String username, String emailAddress, String contactNo, String password, String accommodationName, String address) {
        if (checkAccountRedundancy(username, emailAddress, contactNo, "Accommodation")) {
            //Handle redundancy case, e.g., throw an exception or return null
            return null;
        }
        Accommodation accommodation = new Accommodation(username, emailAddress, contactNo, password, accommodationName, address);
        Main.debug("UserAuthService", "Put into Accommodation DataStore: " + accommodation.getUserID()); // Debugging output
        AccommodationDAO.saveAllAccommodations(); // Save the new accommodation to the data store
        Main.debug("UserAuthService", "Saved All Accommodation DataStore to CSV"); // Debugging output
        AccommodationDAO.retrieveAllAccommodations(); // Ensure the data store is updated
        Main.debug("UserAuthService", "Retrieved All Accommodation DataStore from CSV"); // Debugging output
        Main.debug("UserAuthService", "Accommodation registered: " + accommodation.getUsername()); // Debugging output
        Main.debug("UserAuthService", "isAccommodation registered: " + DataStore.accommodations.get(accommodation.getEmailAddress())); // Debugging output
        return DataStore.accommodations.get(emailAddress);
    }

    public boolean checkAccountRedundancy(String username, String emailAddress, String contactNo, String type) {
        if (type.equals("Guest")) {
            for (Guest guest : DataStore.guests.values()) {
                if (guest.getUsername().equals(username) || guest.getEmailAddress().equals(emailAddress) || guest.getContactNo().equals(contactNo)) {
                    return true; // Redundant account found
                }
            }
        } else if (type.equals("Accommodation")) {
            for (Accommodation accommodation : DataStore.accommodations.values()) {
                if (accommodation.getUsername().equals(username) || accommodation.getEmailAddress().equals(emailAddress) || accommodation.getContactNo().equals(contactNo)) {
                    return true; // Redundant account found
                }
            }
        }
        return false;
    }

}
