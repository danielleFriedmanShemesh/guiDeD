package com.example.guided.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    public static String convertDateToFullFormat(String inputDateString) {
        // Define the input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        try {
            // Parse the input date string to a Date object
            Date date = inputFormat.parse(inputDateString);

            // Format the Date object to the desired output format
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }

    public static String convertFullFormatToDate(String inputDateString) {
        // Define the input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        try {
            // Parse the input date string to a Date object
            Date date = inputFormat.parse(inputDateString);

            // Format the Date object to the desired output format
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
}
