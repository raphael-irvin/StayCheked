package com.example.staycheked.dao;

import java.util.ArrayList;

import com.example.staycheked.Main;
import com.example.staycheked.model.DataStore;
import com.example.staycheked.model.object.FAQ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FAQDAO {

    private static final String DATA_SOURCE = "data/faq.csv";

    // Method to initialize FAQ data
    public static boolean initialize() {
        retrieveAllFAQs();
        return true;
    }

    // Method to save all FAQs
    public static boolean saveAllFAQs() {
        // Implementation for saving FAQs to the DATA_SOURCE
        // This method should write the FAQ data to the CSV file

        Main.debug("FAQDAO", "Saving FAQs to: " + DATA_SOURCE); // Debugging output
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_SOURCE))) {
            String header = "category,question,answer"; // Assuming these are the fields in the FAQ
            writer.write(header);
            writer.newLine();
            for (FAQ faq : DataStore.faqs) {
                // Escape commas in the category, question, and answer to avoid CSV format issues
                String faqCategory = faq.getCategory().replace(",", "$"); // Escape commas in category
                String faqQuestion = faq.getQuestion().replace(",", "$"); // Escape commas in question
                String faqAnswer = faq.getAnswer().replace(",", "$"); // Escape commas in answer

                // Join the fields with commas and write to the file
                String line = String.join(",", faqCategory, faqQuestion, faqAnswer);
                writer.write(line);
                writer.newLine();
                Main.debug("FAQDAO", "Saved FAQ: " + line); // Debugging output
            }

        return true; // Return true if successful, false otherwise
        } catch (Exception e) {
            Main.debug("FAQDAO", "Error saving FAQs to file: " + e.getMessage());
            e.printStackTrace();
            return false; // Return false if there was an error writing to the file
        }
    }

    // Method to retrieve all FAQs
    public static boolean retrieveAllFAQs() {
        // Implementation for retrieving FAQs from the DATA_SOURCE
        // This method should read the FAQ data from the CSV file and populate the relevant data structures

        Main.debug("FAQDAO", "Retrieving FAQs from: " + DATA_SOURCE); // Debugging output
        ArrayList<FAQ> faqs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_SOURCE))) {
            String line;
            reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { // Assuming FAQ has 3 fields: question, answer, category
                    String category = parts[0];
                    String rawQuestion = parts[1];
                    String rawAnswer = parts[2];

                    // Escape commas in the question and answer to avoid CSV format issues
                    String question = rawQuestion.replace("$", ",");
                    String answer = rawAnswer.replace("$", ",");

                    FAQ faq = new FAQ(question, answer, category);
                    faqs.add(faq);
                    Main.debug("FAQDAO", "Retrieved FAQ: " + faq); // Debugging output
                    Main.debug("FAQDAO", "FAQ Question: " + faq.getQuestion()); // Debugging output
                    Main.debug("FAQDAO", "FAQ Answer: " + faq.getAnswer()); // Debugging output
                    Main.debug("FAQDAO", "FAQ Category: " + faq.getCategory()); // Debugging output
                }
            }
            Main.debug("FAQDAO", "Total FAQs retrieved: " + faqs.size()); // Debugging output
            DataStore.faqs = faqs; // Assuming DataStore has a static field for FAQs
        } catch (Exception e) {
            Main.debug("FAQDAO", "Error retrieving FAQs from file: " + e.getMessage());
            e.printStackTrace();
            return false; // Return false if there was an error reading the file
        }

        return true; // Return true if successful, false otherwise
    }

}
