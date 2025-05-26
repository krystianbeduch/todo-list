package com.example.todolist.domain.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todolist.domain.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;
    private final MutableLiveData<List<Task>> _tasks = new MutableLiveData<>();
    public LiveData<List<Task>> tasks = _tasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        loadTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void loadTasks() {
        new Thread(() -> _tasks.postValue(repository.getAllTasks())).start();
    }

    public void insert(Task task) {
        new Thread(() -> {
            repository.insert(task);
            loadTasks();
        }).start();
    }

    public void delete(Task task) {
        new Thread(() -> {
            repository.delete(task);
            loadTasks();
        }).start();
    }

    public void update(Task task) {
        new Thread(() -> {
            repository.update(task);
            loadTasks();
        }).start();
    }
}

