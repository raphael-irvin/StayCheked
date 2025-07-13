package com.example.staychecked;

import com.example.staychecked.model.user.User;

public class Session {

    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
