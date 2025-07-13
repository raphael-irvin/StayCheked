module com.example.staychecked {
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

    exports com.example.staychecked.service;

    opens com.example.staychecked to javafx.fxml;
    exports com.example.staychecked;
    exports com.example.staychecked.model.user;
    opens com.example.staychecked.model.user to javafx.fxml;
    exports com.example.staychecked.model.object;
    opens com.example.staychecked.model.object to javafx.fxml;
    exports com.example.staychecked.model;
    opens com.example.staychecked.model to javafx.fxml;
    exports com.example.staychecked.controller;
    opens com.example.staychecked.controller to javafx.fxml;
    exports com.example.staychecked.controller.lists;
    opens com.example.staychecked.controller.lists to javafx.fxml;
    exports com.example.staychecked.controller.main;
    opens com.example.staychecked.controller.main to javafx.fxml;
}