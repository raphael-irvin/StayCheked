package com.example.staycheked.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Accommodation;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccommodationListController {
    @FXML
    private TableView<Accommodation> accommodationTable;
    @FXML
    private TableColumn<Accommodation, String> accommodationNameColumn;
    @FXML
    private TableColumn<Accommodation, String> emailColumn;
    @FXML
    private TableColumn<Accommodation, String> phoneColumn;
    @FXML
    private TableColumn<Accommodation, String> addressColumn;

    HashMap<String, Accommodation> verifiableAccommodations = new HashMap<>();

    @FXML
    public void initialize() {
        for (Accommodation accommodation : DataStore.accommodations.values()) {
            if (accommodation.getStatus() == true) {
                verifiableAccommodations.put(accommodation.getUserID(), accommodation);
            }
        }

        accommodationNameColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        ObservableList<Accommodation> accommodations = FXCollections.observableArrayList(verifiableAccommodations.values());
        accommodationTable.setItems(accommodations);
    }
}
