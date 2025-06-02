package com.example.todolist.util;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.domain.model.NotificationType;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;

public class NotificationUtils {
    public static final String CHANNEL_ID = "tasks_channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CharSequence name = "Task Notifications";
            String description = "Notifications for due or overdue tasks";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showTaskNotification(Context context, Task task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Uzytkownik nie udzielił zgody na powiadomienia
                return;
            }
        }

        String notificationTitle;
        if (task.getNotificationType() == NotificationType.OVERDUE) {
            notificationTitle = "Minął termin zadania";
        }
        else {
            notificationTitle = "Zbliża się termin zadania";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp) // TODO: Ikonka powiadomien
                .setContentTitle(notificationTitle)
                .setContentText(task.getTitle() + " - termin:\n " + Converters.formatLocalDateTimeToReadableInRecyclerView(task.getDeadline()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(task.getId(), builder.build());
    }


}
