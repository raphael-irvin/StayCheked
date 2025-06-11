package com.example.staycheked.model.user;

public class Admin extends User{

    String role;

    //Constructor for creating new Admin with userID (Initialization)
    public Admin(String userID, String username, String emailAddress, String contactNo, String password) {
        super(username, emailAddress, contactNo, password, "Admin");
        this.setUserID(userID);
    }

    //Constructor for creating new Admin without a userID (RUNTIME)
    public Admin(String username, String emailAddress, String contactNo, String password) {
        super(username, emailAddress, contactNo, password, "Admin");
    }
}
