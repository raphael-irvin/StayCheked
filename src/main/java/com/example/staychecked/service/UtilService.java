package com.example.staychecked.service;

import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilService {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public static String getStringFormattedDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    //Log to File Functionality
    final static String DEBUGFILEPATH = "data/logger/StayChecked_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "_log";
    public static void writeDebugLogToFile(String line) {
        try (
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(DEBUGFILEPATH + ".txt", true))
        ) {
            writer.write(line);
            writer.newLine();
        } catch (Exception e) {
            System.err.println("Error writing to debug log file: " + e.getMessage());
        }
    }
    
}
