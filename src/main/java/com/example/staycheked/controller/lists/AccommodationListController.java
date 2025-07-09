package com.example.staycheked.controller.lists;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

import com.example.staycheked.Session;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.user.Accommodation;
import com.example.staycheked.model.user.Admin;
import com.example.staycheked.model.user.Guest;

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

    //UI for Admin to verify accommodations

    HashMap<String, Accommodation> verifiableAccommodations = new HashMap<>();

    @FXML
    public void initialize() {

        // If the current user is a Guest, show only verified accommodations
        if (Session.getCurrentUser() instanceof Guest) {
            for (Accommodation accommodation : DataStore.accommodations.values()) {
            if (accommodation.getStatus() == true) {
                verifiableAccommodations.put(accommodation.getUserID(), accommodation);
                }
            }
            // Set columns specific to Guest view
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        }

        // If the current user is an Admin, show all accommodations
        if (Session.getCurrentUser() instanceof Admin) {
            for (Accommodation accommodation : DataStore.accommodations.values()) {
                verifiableAccommodations.put(accommodation.getUserID(), accommodation);
            }
            // Set columns specific to Admin view
            phoneColumn.setText("Status"); // Change phone column to Status for Admin view
            phoneColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getStatus() ? "Verified" : "Unverified"
                )
            );
        }

        accommodationNameColumn.setCellValueFactory(new PropertyValueFactory<>("accommodationName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        ObservableList<Accommodation> accommodations = FXCollections.observableArrayList(verifiableAccommodations.values());
        accommodationTable.setItems(accommodations);

        //Set up Row Factory for TableView
        accommodationTable.setRowFactory(_ -> {
            javafx.scene.control.TableRow<Accommodation> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Accommodation selectedAccommodation = row.getItem();
                    boolean isAdmin = Session.getCurrentUser() instanceof Admin;
                    showAccommodationDetailsDialog(selectedAccommodation, isAdmin);
                }
            });
            return row;
        });
    }

    // Dialog for Accommodation Details (Guest: view only, Admin: editable status)
    private void showAccommodationDetailsDialog(Accommodation accommodation, boolean isAdmin) {
        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Accommodation Details");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        javafx.scene.control.Label nameLabel = new javafx.scene.control.Label("Accommodation Name: " + accommodation.getAccommodationName());
        javafx.scene.control.Label emailLabel = new javafx.scene.control.Label("Email Address: " + accommodation.getEmailAddress());
        javafx.scene.control.Label addressLabel = new javafx.scene.control.Label("Address: " + accommodation.getLocation());

        javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(10, nameLabel, emailLabel, addressLabel);

        final String[] originalStatus = new String[1];
        final javafx.scene.control.ComboBox<String>[] statusCombo = new javafx.scene.control.ComboBox[1];

        if (isAdmin) {
            originalStatus[0] = accommodation.getStatus() ? "Verified" : "Unverified";
            statusCombo[0] = new javafx.scene.control.ComboBox<>();
            statusCombo[0].getItems().addAll("Verified", "Unverified");
            statusCombo[0].setValue(originalStatus[0]);
            javafx.scene.layout.HBox statusBox = new javafx.scene.layout.HBox(10, new javafx.scene.control.Label("Status:"), statusCombo[0]);
            vbox.getChildren().add(statusBox);
        }

        dialog.getDialogPane().setContent(vbox);
        javafx.scene.control.ButtonType closeButtonType = new javafx.scene.control.ButtonType("Close", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);
        dialog.setResultConverter(_ -> null);

        if (isAdmin && statusCombo[0] != null) {
            javafx.scene.control.Button closeButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(closeButtonType);
            closeButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
                String newStatus = statusCombo[0].getValue();
                if (!newStatus.equals(originalStatus[0])) {
                    javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Status changed. Save changes?", javafx.scene.control.ButtonType.YES, javafx.scene.control.ButtonType.NO, javafx.scene.control.ButtonType.CANCEL);
                    confirm.setTitle("Confirm Status Change");
                    confirm.initOwner(dialog.getOwner());
                    confirm.showAndWait().ifPresent(response -> {
                        if (response == javafx.scene.control.ButtonType.YES) {
                            accommodation.setStatus("Verified".equals(newStatus));
                        } else if (response == javafx.scene.control.ButtonType.CANCEL) {
                            event.consume();
                        }
                    });
                }
            });
        }
        dialog.showAndWait();
    }
}
