package com.example.todolist.domain.model.enums;

import android.content.Context;
import com.example.todolist.R;

public enum NotificationType {
    UPCOMING,
    OVERDUE,
    NONE;

    public String getTextToNotification(Context context) {
        return switch (this) {
            case UPCOMING -> context.getString(R.string.text_notification_upcoming) + "\n";
            case OVERDUE -> context.getString(R.string.text_notification_overdue) + "\n";
            default -> "";
        };
    }
}