package com.example.todolist.domain.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.todolist.data.dao.TaskDao;
import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.Task;

import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> allTasks;

    public TaskRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
        allTasks = taskDao.getAll();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getById(int id) {
        return taskDao.getById(id);
    }

    public void insert(Task task) {
        taskDao.insert(task);
    }

    public void delete(Task task) {
        taskDao.delete(task);
    }

    public void update(Task task) {
        taskDao.update(task);
    }

    public void deleteAll() {
        taskDao.deleteAll();
    }

    public void changeStatus(int taskId, boolean isDone) {
        taskDao.changeStatus(taskId, isDone);
    }
}
