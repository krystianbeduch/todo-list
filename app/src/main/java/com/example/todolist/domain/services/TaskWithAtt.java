package com.example.todolist.domain.services;

import android.content.Context;

import com.example.todolist.data.dao.AttachmentDao;
import com.example.todolist.data.dao.TaskDao;
import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.Attachment;
import com.example.todolist.domain.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskWithAtt {
    private final TaskDao taskDao;
    private final AttachmentDao attachmentDao;

    public TaskWithAtt(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
        attachmentDao = db.attachmentDao();
    }

    public List<Task> getTasksWithAttachments() {
        List<Task> tasks = taskDao.getAllSync();
        List<Attachment> attachments = attachmentDao.getAllAttachments();

        Map<Integer, List<Attachment>> attachmentMap = new HashMap<>();
        for (Attachment attachment : attachments) {
            int taskId = attachment.getTaskId();
            if (!attachmentMap.containsKey(taskId)) {
                attachmentMap.put(taskId, new ArrayList<>());
            }
            attachmentMap.get(taskId).add(attachment);
        }

        // Przypisanie do tasków
        for (Task task : tasks) {
            List<Attachment> taskAttachments = attachmentMap.getOrDefault(task.getId(), new ArrayList<>());
            task.setAttachments(taskAttachments);
        }
        return tasks;
    }
}
