package com.example.staycheked.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

import com.example.staycheked.Session;
import com.example.staycheked.dao.BookingDAO;
import com.example.staycheked.dao.GuestDAO;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.staycheked.Main;

public class GuestListController {
    @FXML
    private TableView<Guest> guestTable;
    @FXML
    private TableColumn<Guest, String> guestIdColumn;
    @FXML
    private TableColumn<Guest, String> nameColumn;
    @FXML
    private TableColumn<Guest, String> emailColumn;
    @FXML
    private TableColumn<Guest, String> phoneColumn;

    @FXML
    public void initialize() {

        HashMap<String, Guest> matchingVerifiedGuests = new HashMap<>();
        //Retrieve Guests and Booking Data
        GuestDAO.retrieveAllGuests();
        BookingDAO.retrieveAllBookings();

        // Filter guests to only include those that has ever verified a booking in the accommodation
        if (Session.getCurrentUser() instanceof Accommodation) {
            Accommodation currentAccommodation = (Accommodation) Session.getCurrentUser();
            for (Booking booking : currentAccommodation.getBookings()) {
                if (booking.getGuest() != null) {
                    matchingVerifiedGuests.put(booking.getGuest().getUserID(), booking.getGuest());
                }
            }
        } else {
            Main.debug("GuestListController", "Unknown user type.");
        }

        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        ObservableList<Guest> guests = FXCollections.observableArrayList(matchingVerifiedGuests.values());
        guestTable.setItems(guests);
    }
}
