package com.example.todolist.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.ui.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>();

    public HomeViewModel() {
        List<Task> mockTasks = new ArrayList<>();
        mockTasks.add(new Task(1, "Wynieść śmieci", "2025-05-27", false));
        mockTasks.add(new Task(2, "Spotkanie z zespołem", "2025-05-28", true));
        mockTasks.add(new Task(3, "Zrobić zakupy", "2025-05-29", false));
        tasks.setValue(mockTasks);
    }

    public LiveData<List<Task>> getTaskList() {
        return tasks;
    }
}