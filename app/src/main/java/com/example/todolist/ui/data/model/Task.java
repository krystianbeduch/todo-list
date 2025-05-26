package com.example.todolist.ui.data.model;

public class Task {
    private int id;
    private String title;
    private String deadLine;
    private boolean isDone;

    public Task(int id, String title, String deadLine, boolean isDone) {
        this.id = id;
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
