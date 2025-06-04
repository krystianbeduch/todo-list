package com.example.todolist.domain.model.enums;

import android.content.Context;

import androidx.annotation.StringRes;

import com.example.todolist.R;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
    HIGH(0, R.string.high_priority),
    MEDIUM(1, R.string.medium_priority),
    LOW(2, R.string.low_priority);

    private final int value;
    @StringRes
    private final int stringResId;

    public static Priority fromInt(int value) {
        for (Priority p : values()) {
            if (p.getValue() == value) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + value);
    }

    public static Priority fromDisplayName(Context context, String displayName) {
        for (Priority p : values()) {
            if (context.getString(p.getStringResId()).equals(displayName)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }

    public static int getPriorityIndex(Priority priority) {
        return priority.getValue();
    }
}