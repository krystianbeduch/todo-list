package com.example.todolist.domain.model;

import android.content.Context;

import androidx.annotation.StringRes;

import com.example.todolist.R;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
    CREATED_DATE(R.string.sort_created_at),
    TITLE(R.string.sort_title),
    DEADLINE(R.string.sort_deadline),
    PRIORITY(R.string.sort_priority),
    STATUS(R.string.sort_status);

    @StringRes
    private final int stringResId;

    public static SortType fromDisplayName(Context context, String name) {
        for (SortType type : values()) {
            if (context.getString(type.getStringResId()).equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + name);
    }
}