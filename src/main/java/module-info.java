module com.example.staycheked {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires jdk.jshell;
    requires java.desktop;
    requires javafx.base;
    requires langchain4j;
    requires langchain4j.core;
    requires langchain4j.open.ai;
    requires langchain4j.easy.rag;
    requires java.sql;
    requires java.net.http;
    requires org.apache.logging.log4j;
    requires org.slf4j;

    exports com.example.staycheked.service;

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
    exports com.example.staycheked.controller.lists;
    opens com.example.staycheked.controller.lists to javafx.fxml;
    exports com.example.staycheked.controller.main;
    opens com.example.staycheked.controller.main to javafx.fxml;
}