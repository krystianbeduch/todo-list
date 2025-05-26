package com.example.todolist.domain.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    @ColumnInfo(name = "dead_line")
    private String deadLine;
    @ColumnInfo(name = "is_done")
    private boolean isDone;

//    public Task(int id, String title, String deadLine, boolean isDone) {
//        this.id = id;
//        this.title = title;
//        this.deadLine = deadLine;
//        this.isDone = isDone;
//    }

    public Task(String title, String deadLine, boolean isDone) {
        this.title = title;
        this.deadLine = deadLine;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
