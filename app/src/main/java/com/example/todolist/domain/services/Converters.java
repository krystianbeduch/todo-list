package com.example.todolist.domain.services;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.todolist.domain.model.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class Converters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter taskRecyclerViewFormatter = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter formFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    @TypeConverter
    public static LocalDateTime fromStringToLocalDateTimeISO(String value) {
        return value == null ? null : LocalDateTime.parse(value, formatter);
    }

    @TypeConverter
    public static String fromLocalDateTimeISOToString(LocalDateTime dateTime) {
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

    @TypeConverter
    public static List<String> fromStringToList(String value) {
        return Arrays.asList(value.split("\\|"));
    }

    @TypeConverter
    public static String fromListToString(List<String> list) {
        return TextUtils.join("|", list);
    }

    public static String formatLocalDateTimeToReadableInRecyclerView(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(taskRecyclerViewFormatter);
    }

    public static LocalDateTime fromStringToLocalDateTime(String dateTimeText) throws DateTimeParseException {
        if (dateTimeText == null || dateTimeText.isEmpty()) {
            throw new DateTimeParseException("Pusty tekst daty", dateTimeText, 0);
        }
        return LocalDateTime.parse(dateTimeText, formFormatter);
    }
    public static String fromLocalDateTimeToString(LocalDateTime dateTime) {
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
