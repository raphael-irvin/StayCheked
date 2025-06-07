package com.example.staycheked.service;

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
        if (checkAccountRedundancy(username, emailAddress, contactNo)) {
            // Handle redundancy case, e.g., throw an exception or return null
            return null;
        }
        return new Guest(username, emailAddress, contactNo, password, fullName);
    }

    public Accommodation registerAccommodation(String username, String emailAddress, String contactNo, String password, String accommodationName, String address) {
        if (checkAccountRedundancy(username, emailAddress, contactNo) && checkAccommodationRedundancy(accommodationName)) {
            // Handle redundancy case, e.g., throw an exception or return null
            return null;
        }
        return new Accommodation(username, emailAddress, contactNo, password, accommodationName, address);
    }

    public boolean checkAccountRedundancy(String username, String emailAddress, String contactNo) {
        //Check if there is any User with the same username, emailAddress or contactNo
        //Return true if there is a redundancy, else return false
        return false; // Placeholder return value
    }

    public boolean checkAccommodationRedundancy(String accommodationName) {
        //Check if there is any Accommodation with the same accommodationName
        //Return true if there is a redundancy, else return false
        return false; // Placeholder return value
    }

}
