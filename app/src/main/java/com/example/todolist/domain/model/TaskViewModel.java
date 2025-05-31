package com.example.todolist.domain.model;
import com.example.todolist.domain.repository.AttachmentRepository;
import com.example.todolist.domain.repository.TaskRepository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    private final MediatorLiveData<List<Task>> tasks;
    private LiveData<List<Task>> currentSource;
    private SortType currentSortType;
//    private final LiveData<List<Task>> tasks;


    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        attachmentRepository = new AttachmentRepository(application);
        tasks = new MediatorLiveData<>();
        currentSortType = SortType.CREATED_DATE;

//        tasks = taskRepository.getSortedTasks(SortType.CREATED_DATE);
        loadTasksBySort(SortType.CREATED_DATE);
    }

    public void loadTasksBySort(SortType sortType) {
        currentSortType = sortType;
        LiveData<List<Task>> newSource = taskRepository.getSortedTasks(sortType);
        if (currentSource != null) {
            tasks.removeSource(currentSource);
        }
        currentSource = newSource;
//        tasks.addSource(newSource, tasks::setValue);

        tasks.addSource(newSource, list -> {
            if (list != null) {
                List<Task> sortedList = new ArrayList<>(list);
                sortTasks(sortedList, currentSortType);
            }
            else {
                tasks.setValue(null);
            }
        });
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public LiveData<Task> getTaskById(int id) {
        return taskRepository.getById(id);
    }

    public void insert(Task task) {
        new Thread(() -> {
            taskRepository.insert(task);
//            new Handler(Looper.getMainLooper()).post(() -> {
//                loadTasksBySort(currentSortType);
//            });
        }).start();
    }

    public void delete(Task task) {
        new Thread(() -> {
            taskRepository.delete(task);
        }).start();
    }

    public void update(Task task) {
        new Thread(() -> {
            taskRepository.update(task);
        }).start();
//        loadTasksBySort(currentSortType);
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
            new Handler(Looper.getMainLooper()).post(() -> {
                List<Task> currentTasks = tasks.getValue();
//                if (currentTasks != null) {
//                    List<Task> newTasks = new ArrayList<>(currentTasks);
//                    for (Task t : newTasks) {
//                        if (t.getId() == task.getId()) {
//                            t.setDone(!t.isDone());
//                            break;
//                        }
//                    }
//                    Log.i("curet", currentSortType.name());
                    sortTasks(currentTasks, currentSortType);
//                    tasks.setValue(currentTasks);
//                }

//                loadTasksBySort(currentSortType);
            });
        }).start();
//        loadTasksBySort(currentSortType);
        Log.i("df", currentSortType.toString());
    }


    public void sortTasks(List<Task> tasks, SortType sortType) {
        Comparator<Task> comparator;
        switch (sortType) {
            case TITLE:
                comparator = Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER);
                break;
            case DEADLINE:
                comparator = Comparator.comparing(Task::getDeadline);
                break;
            case PRIORITY:
                comparator = Comparator.comparing(Task::getPriority);
                break;
            case STATUS:
                comparator = Comparator.comparing(Task::isDone);
                break;
            case CREATED_DATE:
            default:
                comparator = Comparator.comparing(Task::getCreatedAt).reversed();
        }
        tasks.sort(comparator);
        currentSortType = sortType;
        this.tasks.setValue(tasks);
    }

    public void addAttachmentToTask(Attachment attachment) {
        new Thread(() -> {
            attachmentRepository.insertAttachment(attachment);
        }).start();
    }

}

