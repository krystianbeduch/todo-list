package com.example.todolist.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
    CREATED_DATE("Data dodania"),
    TITLE("Tytuł"),
    DEADLINE("Termin"),
    PRIORITY("Priorytet"),
    STATUS("Status");

    private final String displayName;

    public static SortType fromDisplayName(String name) {
        for (SortType type : values()) {
            if (type.getDisplayName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + name);
    }
}