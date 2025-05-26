package com.example.todolist.domain.repository;

import android.content.Context;

import com.example.todolist.data.dao.TaskDao;
import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.Task;

import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;

    public TaskRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
    }

    public List<Task> getAllTasks() {
        return taskDao.getAll();
    }

    public void insert(Task task) {
        new Thread(() -> taskDao.insert(task)).start();
    }

    public void delete(Task task) {
        new Thread(() -> taskDao.delete(task)).start();
    }

    public void update(Task task) {
        new Thread(() -> taskDao.update(task)).start();
    }
}
