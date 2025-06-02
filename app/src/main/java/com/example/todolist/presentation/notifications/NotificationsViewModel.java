//package com.example.todolist.presentation.notifications;
//
//import android.app.Application;
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.todolist.domain.model.SortType;
//import com.example.todolist.domain.model.Task;
//import com.example.todolist.domain.repository.TaskRepository;
//import com.example.todolist.util.NotificationUtils;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//public class NotificationsViewModel extends AndroidViewModel {
//
////    private final MutableLiveData<String> mText;
////    private final TaskRepository taskRepository;
//    private final TaskRepository taskRepository;
//    private LiveData<List<Task>> upcomingTasks;
////    private String deadlineLimitStr;
//
//    public NotificationsViewModel(@NonNull Application application) {
//        super(application);
//        taskRepository = new TaskRepository(application);
//
////        long now = System.currentTimeMillis();
////        deadlineLimit = now + 24 * 60 * 60 * 1000; // 24h from now
//
////        upcomingTasks = taskRepository.getTasksWithUpcomingDeadline(deadlineLimit);
//
//
////        mText = new MutableLiveData<>();
////        mText.setValue("This is notifications fragment");
//    }
//
////    public LiveData<List<Task>> getAllTasks() {
////        return allTasks;
////    }
//
////    public void checkAndNotifyDueTasks(Context context) {
////        new Thread(() -> {
////            List<Task> allTasks = taskRepository.getSortedTasks(SortType.CREATED_DATE);
////
////            LocalDate today = LocalDate.now();
////            for (Task task : allTasks) {
////                if (task.getDueDate() != null &&
////                        (task.getDueDate().isEqual(today) || task.getDueDate().isBefore(today)) &&
////                        !task.isCompleted()) {
////                    NotificationUtils.showTaskNotification(context, task);
////                }
////            }
////        }
////        ).start();
////    }
//
//
//    public LiveData<List<Task>> getUpcomingTasks() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime limit = now.plusHours(24);
//        String deadlineLimitStr = limit.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        return taskRepository.getTasksWithUpcomingDeadline(deadlineLimitStr);
//    }
//}