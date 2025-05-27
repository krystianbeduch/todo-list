package com.example.todolist.domain.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todolist.domain.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository taskRepository;
//    private final MutableLiveData<List<Task>> _tasks = new MutableLiveData<>();
    private final LiveData<List<Task>> tasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        tasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

//    public void loadTasks() {
//        new Thread(() -> _tasks.postValue(taskRepository.getAllTasks())).start();
//    }

    public void insert(Task task) {
        new Thread(() -> {
            taskRepository.insert(task);
//            loadTasks();
        }).start();
    }

    public void delete(Task task) {
        new Thread(() -> {
            taskRepository.delete(task);
//            loadTasks();
        }).start();
    }

    public void update(Task task) {
        new Thread(() -> {
            taskRepository.update(task);
//            loadTasks();
        }).start();
    }

    public void deleteAll(Runnable onFinished) {
        new Thread(() -> {
            taskRepository.deleteAll();
            onFinished.run();
        }).start();
    }

    public void changeStatus(Task task) {
        new Thread(() -> {
            taskRepository.changeStatus(task.getId(), !task.isDone());
        }).start();
    }
}

