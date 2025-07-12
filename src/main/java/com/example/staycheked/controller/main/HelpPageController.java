package com.example.staycheked.controller.main;

import com.example.staycheked.service.ChatbotService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HelpPageController {

    @FXML
    VBox chatBotMessageContainer;
    @FXML
    TextField chatBotField;
    @FXML
    Button chatBotSendButton;
    @FXML
    Label chatBotWelcomeMessage;

    private ChatbotService chatbotService;

    // Initialization method
    public void initialize() {
        // Set up the UI components and event handlers
        chatBotSendButton.setOnAction(this::onChatBotSendButtonClick);

        if (chatbotService == null) {
            chatBotField.setDisable(true);
            chatBotSendButton.setDisable(true);
            chatBotWelcomeMessage.setText("I'm sorry, but the chatbot service is currently unavailable. Please check your API keys or internet connection.");
        }
    }

    // Constructor
    public HelpPageController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // Method to handle chat bot send button click
    private void onChatBotSendButtonClick(ActionEvent event) {
        String userMessage = chatBotField.getText();

        if (userMessage.isEmpty()) {
            // Display Dialog or Alert for empty message
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Error");
            dialog.setContentText("Please enter a message before sending.");
            dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);
            dialog.showAndWait();
            return;
        }

        // Show the user message in the chat bot message container
        Label sentMessage = new Label(userMessage);
        sentMessage.setWrapText(true);
        sentMessage.getStyleClass().add("message-bubble");
        sentMessage.getStyleClass().add("message-bubble-sent");

        //Create a Hbox Container for the message to be shown on the right side
        HBox messageContainer = new HBox();
        //Create a pane for the message to be shown on the right side
        Pane messagePane = new Pane();
        
        HBox.setHgrow(messagePane, javafx.scene.layout.Priority.ALWAYS);
        messageContainer.getChildren().add(messagePane);
        messageContainer.getChildren().add(sentMessage);
        chatBotMessageContainer.getChildren().add(messageContainer);

        //Process the user message
        // Get the bot response from the chatbot service
        String botResponse = chatbotService.getResponse(userMessage);

        //Show the bot response in the chat bot message container
        Label receivedMessage = new Label(botResponse);
        receivedMessage.setWrapText(true);
        receivedMessage.getStyleClass().add("message-bubble");
        receivedMessage.getStyleClass().add("message-bubble-received");
        chatBotMessageContainer.getChildren().add(receivedMessage);

        // Clear the input field
        chatBotField.clear();
    }

}
