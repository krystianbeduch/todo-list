package com.example.todolist.domain.model;

public enum NotificationType {
    UPCOMING,
    OVERDUE,
    NONE;

    public String getTextToNotification() {
        switch (this) {
            case UPCOMING: return "Zbliżający się termin zadania\n";
            case OVERDUE: return "Minął termin zadania\n";
            default: return "";
        }
    }
}
