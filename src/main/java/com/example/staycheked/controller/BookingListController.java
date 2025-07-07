package com.example.staycheked.controller;

import java.util.HashMap;

import com.example.staycheked.Session;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Guest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookingListController {
    
    //General Shared Columns
    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, String> bookingIdColumn;
    @FXML
    private TableColumn<Booking, String> guestLastNameColumn;
    @FXML
    private TableColumn<Booking, String> roomColumn;
    @FXML
    private TableColumn<Booking, String> statusColumn;

    //Specific Columns for Guest and Accommodation
    @FXML
    private TableColumn<Booking, String> guestColumn;
    @FXML
    private TableColumn<Booking, String> accommodationColumn;

    @FXML
    public void initialize() {
        HashMap<String, Booking> matchingBookings = new HashMap<>();

        //Guest user
        if (Session.getCurrentUser() instanceof Guest) {
            // Filter bookings for the current guest
            Guest currentGuest = (Guest) Session.getCurrentUser();
            for (Booking booking : DataStore.bookings.values()) {
                if (booking.getGuest() != null && booking.getGuest().getUserID().equals(currentGuest.getUserID())) {
                    matchingBookings.put(booking.getBookingID(), booking);
                }
            }
            //Set Collumns Specific to Guest
            accommodationColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getAccommodation() != null ? cellData.getValue().getAccommodation().getUsername() : "N/A"
                )
            );
        } 
        
        // Accommodation user
        else if (Session.getCurrentUser() instanceof Accommodation) {
            // Filter bookings for the current accommodation
            Accommodation currentAccommodation = (Accommodation) Session.getCurrentUser();
            for (Booking booking : DataStore.bookings.values()) {
                if (booking.getAccommodation() != null && booking.getAccommodation().getUserID().equals(currentAccommodation.getUserID())) {
                    matchingBookings.put(booking.getBookingID(), booking);
                }
            }
            //Set Collumns Specific to Accommodation
            guestColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getGuest() != null ? cellData.getValue().getGuest().getUsername() : "N/A"
                )
            );
        } 
        
        // Unknown user type
        else {
            System.out.println("Unknown user type.");
        }

        //Set General Columns
        guestLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestIdentificationLastName"));
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        //Put Bookings into Table
        ObservableList<Booking> Bookings = FXCollections.observableArrayList(matchingBookings.values());
        bookingTable.setItems(Bookings);
    }

}
