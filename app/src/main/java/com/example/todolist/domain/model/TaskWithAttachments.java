package com.example.todolist.domain.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TaskWithAttachments {
    @Embedded
    public Task task;

    @Relation(
            parentColumn = "id",
            entityColumn = "task_id"
    )
    public List<Attachment> attachments;
}
