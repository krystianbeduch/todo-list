package com.example.todolist.util.file;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;

import java.io.IOException;

public class TaskTypeJsonAdapter extends TypeAdapter<Task> {
    @Override
    public void write(JsonWriter out, Task task) throws IOException {
        out.beginObject();
        out.name("id").value(task.getId());
        out.name("title").value(task.getTitle());
        out.name("deadline").value(Converters.fromLocalDateTimeToString(task.getDeadline()));
        out.name("priority").value(task.getPriority().getDisplayName());
        out.name("isDone").value(task.isDone());
        out.name("createdAt").value(Converters.fromLocalDateTimeToString(task.getCreatedAt()));
        out.endObject();
    }

    @Override
    public Task read(JsonReader in) throws IOException {
        Task task = new Task();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "title":
                    task.setTitle(in.nextString());
                    break;
                case "deadline":
                    task.setDeadline(Converters.fromStringToLocalDateTime(in.nextString()));
                    break;
                case "priority":
                    task.setPriority(Priority.fromDisplayName(in.nextString()));
                    break;
                case "isDone":
                    task.setDone(in.nextBoolean());
                    break;
                case "createdAt":
                    task.setCreatedAt(Converters.fromStringToLocalDateTime(in.nextString()));
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        return task;
    }
}
