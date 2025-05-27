package com.example.todolist.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String title;
    @ColumnInfo(name = "dead_line")
    private LocalDateTime deadline;
    @ColumnInfo(name = "is_done")
    @NonNull
    private boolean isDone;

    @ColumnInfo(name = "priority")
    @NonNull
    private Priority priority;
    @ColumnInfo(name = "created_at")
    @NonNull
    private LocalDateTime createdAt;

//    public Task(int id, String title, String deadLine, boolean isDone) {
//        this.id = id;
//        this.title = title;
//        this.deadLine = deadLine;
//        this.isDone = isDone;
//    }


    public Task(@NonNull String title, LocalDateTime deadline, boolean isDone, @NonNull Priority priority, @NonNull LocalDateTime createdAt) {
        this.title = title;
        this.deadline = deadline;
        this.isDone = isDone;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @NonNull
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(@NonNull Priority priority) {
        this.priority = priority;
    }

    @NonNull
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
