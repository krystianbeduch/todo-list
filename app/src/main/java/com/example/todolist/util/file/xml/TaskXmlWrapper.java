package com.example.todolist.util.file.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Tasks", strict = false)
public class TaskXmlWrapper {
    @ElementList(name = "Tasks", entry = "Task", inline = true)
    private List<TaskXml> taskXmlList;

    public TaskXmlWrapper() {}

    public TaskXmlWrapper(List<TaskXml> taskXmlList) {
        this.taskXmlList = taskXmlList;
    }

    public List<TaskXml> getTaskXmlList() {
        return taskXmlList;
    }
}
