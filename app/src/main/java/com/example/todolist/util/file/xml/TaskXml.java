package com.example.todolist.util.file.xml;

import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.util.converter.Converters;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Root(name = "Task")
public class TaskXml {
    @Element(required = false)
    private int id;
    @Element
    private String title;
    @Element
    private String deadline;
    @Element
    private String priority;
    @Element
    private boolean isDone;
    @Element
    private String createdAt;

    public TaskXml(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.deadline = Converters.fromLocalDateTimeToString(task.getDeadline());
        this.priority = task.getPriority().getDisplayName();
        this.isDone = task.isDone();
        this.createdAt = Converters.fromLocalDateTimeToString(task.getCreatedAt());
    }

    public Task toTask() {
        Task task = new Task();
        task.setTitle(this.title);
        task.setDeadline(Converters.fromStringToLocalDateTime(this.deadline));
        task.setPriority(Priority.fromDisplayName(this.priority));
        task.setDone(this.isDone);
        task.setCreatedAt(Converters.fromStringToLocalDateTime(this.createdAt));
        return task;
    }
}
