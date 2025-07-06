package com.example.staycheked.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Guest;
import javafx.scene.control.cell.PropertyValueFactory;

public class GuestController {
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
        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        ObservableList<Guest> guests = FXCollections.observableArrayList(DataStore.guests.values());
        guestTable.setItems(guests);
    }
}
