package com.studymate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/*
 * Common input checks used by the forms.
 *
 * Every method returns null if the value is okay, otherwise the message
 * that should be shown to the user. Keeps the if-else mess out of the UI.
 */
public final class ValidationUtil {

    private ValidationUtil() {
        // no objects of this class
    }

    // Empty text and spaces-only text both count as missing.
    public static String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            return field + " is required.";
        }
        return null;
    }

    public static String date(String value) {
        try {
            LocalDate.parse(value.trim());
            return null;
        } catch (DateTimeParseException ex) {
            return "Date must use YYYY-MM-DD format.";
        }
    }

    // The caller passes the same formatter it used to display the value,
    // so the error message can just point back at what is on screen.
    public static String dateTime(String value, DateTimeFormatter formatter) {
        try {
            LocalDateTime.parse(value.trim(), formatter);
            return null;
        } catch (DateTimeParseException ex) {
            return "Invalid date/time. Use the displayed format.";
        }
    }

    public static String nonNegativeInt(String value, String field) {
        Integer number = toInt(value);
        if (number == null) {
            return field + " must be a whole number.";
        }
        if (number < 0) {
            return field + " cannot be negative.";
        }
        return null;
    }

    public static String positiveInt(String value, String field) {
        Integer number = toInt(value);
        if (number == null) {
            return field + " must be a whole number.";
        }
        if (number <= 0) {
            return field + " must be greater than zero.";
        }
        return null;
    }

    // Returns null instead of throwing, so the checks above stay readable.
    private static Integer toInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}