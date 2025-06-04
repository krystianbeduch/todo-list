package com.example.todolist.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.todolist.domain.model.enums.NotificationType;
import com.example.todolist.domain.model.enums.Priority;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String title;
    private LocalDateTime deadline;
    @ColumnInfo(name = "is_done")
    private boolean isDone;

    @ColumnInfo(name = "priority")
    @NonNull
    private Priority priority;
    @ColumnInfo(name = "created_at")
    @NonNull
    private LocalDateTime createdAt;

    @Ignore
    private List<Attachment> attachments;

    @Ignore
    private NotificationType notificationType;

    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DEADLINE = "deadline";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_IS_DONE = "isDone";
    public static final String FIELD_CREATED_AT = "createdAt";

    @Ignore
    public Task() {
        title = "";
        priority = Priority.HIGH;
        createdAt = LocalDateTime.now();
    }

    public Task(@NonNull String title, LocalDateTime deadline, boolean isDone, @NonNull Priority priority, @NonNull LocalDateTime createdAt) {
        this.title = title;
        this.deadline = deadline;
        this.isDone = isDone;
        this.priority = priority;
        this.createdAt = createdAt;
        this.notificationType = NotificationType.NONE;
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
