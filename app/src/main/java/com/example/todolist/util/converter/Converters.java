package com.example.todolist.util.converter;

import androidx.room.TypeConverter;

import com.example.todolist.domain.model.Priority;
import com.example.todolist.util.lang.LocalHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Converters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter formatterWithDayName = DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy HH:mm");
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

    public static String formatLocalDateTimeToStringWithDayName(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(formatterWithDayName);
    }

    public static LocalDateTime fromStringToLocalDateTime(String dateTimeText) throws DateTimeParseException {
        if (dateTimeText == null || dateTimeText.isEmpty()) {
            throw new DateTimeParseException("Empty date text", dateTimeText, 0);
        }
        return LocalDateTime.parse(dateTimeText, formFormatter);
    }
    public static String fromLocalDateTimeToString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(formFormatter);
    }
}
