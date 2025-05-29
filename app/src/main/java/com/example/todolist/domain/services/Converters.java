package com.example.todolist.domain.services;

import androidx.room.TypeConverter;

import com.example.todolist.domain.model.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Converters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter taskRecyclerViewFormatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter formFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    @TypeConverter
    public static LocalDateTime fromStringToLocalDateTime(String value) {
        return value == null ? null : LocalDateTime.parse(value, formatter);
    }

    @TypeConverter
    public static String fromLocalDateTimeToString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(formatter);
    }

    @TypeConverter
    public static Integer priorityToInt(Priority priority) {
        return priority == null ? null : priority.getValue();
    }

    @TypeConverter
    public static Priority fromIntToPriority(Integer value) {
        return value == null ? null : Priority.fromInt(value);
    }

    public static String formatLocalDateTimeToReadableInRecyclerView(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(taskRecyclerViewFormatter);
    }

    public static LocalDateTime fromTaskFormDeadlineToLocalDateTime(String dateTimeText) throws DateTimeParseException {
        if (dateTimeText == null || dateTimeText.isEmpty()) {
            throw new DateTimeParseException("Pusty tekst daty", dateTimeText, 0);
        }
        return LocalDateTime.parse(dateTimeText, formFormatter);
    }
    public static String fromLocalDateTimeToStringInEditActivity(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(formFormatter);
    }





//    @TypeConverter
//    public static Priority fromString(String value) {
//        return Priority.valueOf(value);
//    }
//    @TypeConverter
//    public static String priorityToString(Priority priority) {
//        return priority.name();
//    }
}
