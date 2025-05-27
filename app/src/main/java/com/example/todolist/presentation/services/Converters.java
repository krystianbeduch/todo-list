package com.example.todolist.presentation.services;

import androidx.room.TypeConverter;

import com.example.todolist.domain.model.Priority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @TypeConverter
    public static LocalDateTime fromStringToLocalDateTime(String value) {
        return value == null ? null : LocalDateTime.parse(value, formatter);
    }

    @TypeConverter
    public static String localDateTImeToString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(formatter);
    }

    @TypeConverter
    public static Integer priorityToInt(Priority priority) {
        return priority == null ? null : priority.getValue();
    }

    @TypeConverter
    public static Priority fromIntToInt(Integer value) {
        return value == null ? null : Priority.fromInt(value);
    }

    public static String formatReadable(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy HH:mm");
        return dateTime.format(formatter1);
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
