package com.example.staycheked.service;

import java.util.ArrayList;

import com.example.staycheked.Main;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;

public class ChatbotService {

    private ArrayList<String> apiKeys = new ArrayList<>() {
        {
            add("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");
            add("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");
            add("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");
            add("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");
            add("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");
            add("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");
            add("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");
        }
    };

    private OpenAiChatModel chatModel;

    public ChatbotService() {

        // Initialize chatModel with the available API keys
        boolean workingChatModelFound = false;

        for (String key : apiKeys) {
            try {
                chatModel = OpenAiChatModel.builder()
                    .apiKey(key)
                    .modelName(OpenAiChatModelName.GPT_4_O_MINI)
                    .build();
                if (testChatBot()) {
                    workingChatModelFound = true;
                    Main.debug("ChatbotService", "Chat model initialized successfully with key: " + apiKeys.indexOf(key));
                    break; // Exit loop if a working key is found
                }
            }
            catch (Exception e) {
                Main.debug("ChatbotService", "Failed to initialize chat model with key: " + apiKeys.indexOf(key));
                Main.debug("ChatbotService", e.getMessage()); // Log the exception message
                continue; // Try the next key
            }
        }

        if (!workingChatModelFound) {
            throw new RuntimeException("No working chat model found. Please check your API keys.");
        }
    }

    public String getRawResponse(String prompt) {
        return chatModel.chat(prompt);
    }

    public Boolean testChatBot() {
        try {
            String testResponse = chatModel.chat("This is a test message to check if the chatbot is working.");
            Main.debug("ChatbotService", "Test response: " + testResponse); // Debugging output
            return testResponse != null && !testResponse.isEmpty();
        } catch (Exception e) {
            throw e;
        }
    }


}
