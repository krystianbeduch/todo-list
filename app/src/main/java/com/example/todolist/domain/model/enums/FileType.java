package com.example.todolist.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType {
    CSV("text/csv", ".csv"),
    JSON("application/json", ".json"),
    XML("application/xml", ".xml");

    private final String mimeType;
    private final String extension;
}