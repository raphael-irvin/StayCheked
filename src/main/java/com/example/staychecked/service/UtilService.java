package com.example.staychecked.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilService {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public static String getStringFormattedDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }
    
}
