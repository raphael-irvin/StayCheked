package com.example.staycheked.controller;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.Booking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookingListController {

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
    @FXML
    private TableColumn<Booking, String> guestColumn;

    @FXML
    public void initialize() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        guestLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestIdentificationLastName"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        guestColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getGuest() != null ? cellData.getValue().getGuest().getUsername() : "N/A"
                )
            );

        ObservableList<Booking> Bookings = FXCollections.observableArrayList(DataStore.bookings.values());
        bookingTable.setItems(Bookings);
    }

}
