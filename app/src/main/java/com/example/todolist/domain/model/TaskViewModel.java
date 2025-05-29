package com.example.todolist.domain.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todolist.domain.repository.TaskRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository taskRepository;
//    private final MutableLiveData<List<Task>> _tasks = new MutableLiveData<>();
//    private final LiveData<List<Task>> tasks;
    private final MutableLiveData<SortType> sortType;
    private final MediatorLiveData<List<Task>> sortedTasks;

    public void loadTasksBySort(SortType sortType) {
        LiveData<List<Task>> newSource = taskRepository.getSortedTasks(sortType);
        sortedTasks.addSource(newSource, sortedTasks::setValue);
    }

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        sortType = new MutableLiveData<>(SortType.CREATED_DATE);
        sortedTasks = new MediatorLiveData<>();
//        tasks = taskRepository.getAllTasks();

        loadTasksBySort(SortType.CREATED_DATE);


        // Obserwowanie zrodel
//        sortedTasks.addSource(tasks, taskList -> sortAndPost(taskList, sortType.getValue()));
//        sortedTasks.addSource(sortType, sort -> {
//            Log.i("t", tasks.getValue().toString());
//            sortAndPost(tasks.getValue(), sort);
//                });
    }

//    public LiveData<List<Task>> getTasks() {
//        return tasks;
//    }

    public LiveData<List<Task>> getSortedTasks() {
        return sortedTasks;
    }

    public LiveData<Task> getTaskById(int id) {
        return taskRepository.getById(id);
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

    public void setSortType(SortType sortType) {
        Log.i("TVM", "Zmiana sortwania na: " + sortType);
        sortType.setValue(sortType);
    }

    private void sortAndPost(List<Task> taskList, SortType type) {
        Log.i("s", "siema");
        if (taskList == null) {
            sortedTasks.postValue(Collections.emptyList());
            return;
        }
        List<Task> sorted = new ArrayList<>(taskList);
        switch (type) {
            case TITLE:
                sorted.sort(Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER));
                break;
            case DEADLINE:
                sorted.sort(Comparator.comparing(Task::getDeadline, Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            case PRIORITY:
                sorted.sort(Comparator.comparing(Task::getPriority).reversed());
                break;
            case STATUS:
                sorted.sort(Comparator.comparing(Task::isDone));
                break;
            case CREATED_DATE:
            default:
                sorted.sort(Comparator.comparing(Task::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())));
                break;
        }
        Log.i("TaskViewModel", "Lista po sortowaniu: " + sorted);
        sortedTasks.postValue(sorted);
    }


//    private List<Task> sortTasks


}

