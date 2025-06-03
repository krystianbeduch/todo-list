package com.example.todolist.util.file.xml;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Root(name = "Tasks", strict = false)
public class TaskXmlWrapper {
    @ElementList(name = "Tasks", entry = "Task", inline = true)
    private List<TaskXml> taskXmlList;
}
