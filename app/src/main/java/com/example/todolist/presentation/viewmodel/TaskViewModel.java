package com.example.todolist.presentation.viewmodel;
import com.example.todolist.domain.model.Attachment;
import com.example.todolist.domain.model.FileType;
import com.example.todolist.domain.model.SortType;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.repository.AttachmentRepository;
import com.example.todolist.domain.repository.TaskRepository;
import com.example.todolist.util.file.FileService;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Comparator;
import java.util.List;

import lombok.Getter;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    @Getter
    private final MediatorLiveData<List<Task>> tasks;
    @Getter
    private final MutableLiveData<List<Task>> tasksForNotification = new MutableLiveData<>();
    private LiveData<List<Task>> currentSource;
    private SortType currentSortType;
    @Getter
    private final MutableLiveData<Boolean> hasInsertedDummy = new MutableLiveData<>(false);
    @Getter
    private final MutableLiveData<Boolean> notificationChecked = new MutableLiveData<>(false);

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        attachmentRepository = new AttachmentRepository(application);
        tasks = new MediatorLiveData<>();
        currentSortType = SortType.CREATED_DATE;
        loadTasksBySort(SortType.CREATED_DATE);
    }

    public void loadTasksBySort(SortType sortType) {
        currentSortType = sortType;
        LiveData<List<Task>> newSource = taskRepository.getSortedTasks();
        if (currentSource != null) {
            tasks.removeSource(currentSource);
        }
        currentSource = newSource;
        tasks.addSource(newSource, list -> {
            if (list == null) {
                tasks.setValue(null);
                return;
            }

            AsyncTask.execute(() -> {
                for (Task task : list) {
                    List<Attachment> attachments = attachmentRepository.getAttachmentsByTaskId(task.getId());
                    task.setAttachments(attachments);
                }
                new Handler(Looper.getMainLooper()).post(() -> {
                    sortTasks(list, currentSortType);
                });
            });
        });
    }

    public LiveData<Task> getTaskById(int id) {
        return taskRepository.getById(id);
    }

    public void insert(Task task) {
        AsyncTask.execute(() -> taskRepository.insert(task));
    }

    public void delete(Task task) {
        AsyncTask.execute(() -> taskRepository.delete(task));
    }

    public void update(Task task) {
        AsyncTask.execute(() -> taskRepository.update(task));
    }

    public void deleteAll(Runnable onFinished) {
        AsyncTask.execute(() -> {
            taskRepository.deleteAll();
            onFinished.run();
        });
    }

    public void changeStatus(Task task) {
        AsyncTask.execute(() -> {
            taskRepository.changeStatus(task.getId(), !task.isDone());
            new Handler(Looper.getMainLooper()).post(() -> {
                List<Task> currentTasks = tasks.getValue();
                    sortTasks(currentTasks, currentSortType);
            });
        });
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
        AsyncTask.execute(() -> {
            attachmentRepository.insertAttachment(attachment);

            new Handler(Looper.getMainLooper()).post(() -> {
                loadTasksBySort(currentSortType);
            });
        });
    }

    public void deleteAttachment(Attachment attachment) {
        AsyncTask.execute(() -> {
            attachmentRepository.deleteAttachment(attachment);

            new Handler(Looper.getMainLooper()).post(() -> {
                loadTasksBySort(currentSortType);
            });
        });
    }

    public void updateTasksForNotification(List<Task> tasks) {
        tasksForNotification.setValue(tasks);
    }

    public void markDummyInserted() {
        hasInsertedDummy.setValue(true);
    }

    public void markNotificationChecked() {
        notificationChecked.setValue(true);
    }

    public void importTasksFromFile(Context context, Uri uri, FileType fileType) {
        switch (fileType) {
            case CSV:
                FileService.importTasksFromCsv(context, uri);
                break;
            case JSON:
                FileService.importTasksFromJson(context, uri);
                break;
            case XML:
                FileService.importTasksFromXml(context, uri);
                break;
        }
    }

    public void exportTasksToFile(Context context, FileType fileType) {
        switch (fileType) {
            case CSV:
                FileService.exportTasksToCsv(context);
                break;
            case JSON:
                FileService.exportTasksToJson(context);
                break;
            case XML:
                FileService.exportTasksToXml(context);
                break;
        }
    }
}

