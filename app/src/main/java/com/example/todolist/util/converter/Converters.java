package com.example.todolist.util.converter;

import android.content.Context;

import androidx.room.TypeConverter;

import com.example.todolist.domain.model.Priority;
import com.example.todolist.util.lang.LocalHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class Converters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
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

    public static String formatLocalDateTimeToStringWithDayName(Context context, LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.format(getFormatterWithDayName(context));
    }

    private static DateTimeFormatter getFormatterWithDayName(Context context) {
        String languageCode = LocalHelper.getSavedLanguage(context);
        Locale locale = new Locale(languageCode);
        return DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy HH:mm", locale);
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
