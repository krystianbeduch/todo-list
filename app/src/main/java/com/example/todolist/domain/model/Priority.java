package com.example.todolist.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
    HIGH(0, "Wysoki"),
    MEDIUM(1, "Średni"),
    LOW(2, "Niski");


    private final int value;
    private final String displayName;

    public static Priority fromInt(int value) {
        for (Priority p : values()) {
            if (p.getValue() == value) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + value);
    }

    public static Priority fromDisplayName(String displayName) {
        for (Priority p : values()) {
            if (p.getDisplayName().equals(displayName)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }

    public static int getPriorityIndex(Priority priority) {
        for (Priority p : values()) {
            if (p.getDisplayName().equals(priority.getDisplayName())) {
                return p.getValue();
            }
        }
        return 0;
    }
}
