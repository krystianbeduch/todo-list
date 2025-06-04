package com.example.todolist.util.file;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.example.todolist.domain.model.enums.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.util.converter.Converters;

import java.io.IOException;

public class TaskTypeJsonAdapter extends TypeAdapter<Task> {

    @Override
    public void write(JsonWriter out, Task task) throws IOException {
        out.beginObject();
        out.name(Task.FIELD_ID).value(task.getId());
        out.name(Task.FIELD_TITLE).value(task.getTitle());
        out.name(Task.FIELD_DEADLINE).value(Converters.fromLocalDateTimeToString(task.getDeadline()));
        out.name(Task.FIELD_PRIORITY).value(task.getPriority().name());
        out.name(Task.FIELD_IS_DONE).value(task.isDone());
        out.name(Task.FIELD_CREATED_AT).value(Converters.fromLocalDateTimeToString(task.getCreatedAt()));
        out.endObject();
    }

    @Override
    public Task read(JsonReader in) throws IOException {
        Task task = new Task();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case Task.FIELD_TITLE:
                    task.setTitle(in.nextString());
                    break;
                case Task.FIELD_DEADLINE:
                    task.setDeadline(Converters.fromStringToLocalDateTime(in.nextString()));
                    break;
                case Task.FIELD_PRIORITY:
                    task.setPriority(Priority.valueOf(in.nextString()));
                    break;
                case Task.FIELD_IS_DONE:
                    task.setDone(in.nextBoolean());
                    break;
                case Task.FIELD_CREATED_AT:
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