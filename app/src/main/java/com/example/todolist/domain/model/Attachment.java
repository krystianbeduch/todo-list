package com.example.todolist.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = Task.class,
        parentColumns = "id",       // PK Task
        childColumns = "task_id",   // FK Attachment
        onDelete = ForeignKey.CASCADE
    ),
        indices = {@Index("task_id")}
)
public class Attachment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "task_id")
    private int taskId;
    @NonNull
    private String filename;
    @NonNull
    @ColumnInfo(name = "file_path")
    private String filePath;

    public Attachment(int taskId, @NonNull String filename, @NonNull String filePath) {
        this.taskId = taskId;
        this.filename = filename;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @NonNull
    public String getFilename() {
        return filename;
    }

    public void setFilename(@NonNull String filename) {
        this.filename = filename;
    }

    @NonNull
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@NonNull String filePath) {
        this.filePath = filePath;
    }
}
