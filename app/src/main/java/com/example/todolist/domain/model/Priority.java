package com.example.todolist.domain.model;

public enum Priority {
    HIGH(1, "Wysoki"),
    MEDIUM(2, "Średni"),
    LOW(3, "Niski");


    private final int value;
    private final String displayName;

    Priority(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    //    public String getDisplayName() {
//        return displayName;
//    }

    public static Priority fromInt(int value) {
        for (Priority p : values()) {
            if (p.getValue() == value) {
                return p;
            }
        }
        throw new IllegalArgumentException("Unknown priority: " + value);
    }
}
