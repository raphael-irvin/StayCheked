module com.example.staycheked {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;
    requires java.desktop;


    opens com.example.staycheked to javafx.fxml;
    exports com.example.staycheked;
    exports com.example.staycheked.model.user;
    opens com.example.staycheked.model.user to javafx.fxml;
    exports com.example.staycheked.model.object;
    opens com.example.staycheked.model.object to javafx.fxml;
    exports com.example.staycheked.model;
    opens com.example.staycheked.model to javafx.fxml;
    exports com.example.staycheked.controller;
    opens com.example.staycheked.controller to javafx.fxml;
}