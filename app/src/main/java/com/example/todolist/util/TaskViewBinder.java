package com.example.todolist.util;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;

public class TaskViewBinder {
    public static void bindCommonTaskData(Task task,
                                          TextView titleView,
                                          TextView createdAtView,
                                          TextView priorityView,
                                          TextView doneView,
                                          ImageView attachmentIcon) {
        String createdAtText = "Utworzono: " + Converters.formatLocalDateTimeToReadableInRecyclerView(task.getCreatedAt());
        String priorityText = "Priorytet: " + task.getPriority().getDisplayName();

        titleView.setText(task.getTitle());
        createdAtView.setText(createdAtText);
        priorityView.setText(priorityText);

        switch (task.getPriority()) {
            case HIGH:
                priorityView.setTextColor(Color.RED);
                break;
            case MEDIUM:
                priorityView.setTextColor(Color.rgb(255, 165, 0));
                break;
            case LOW:
                priorityView.setTextColor(Color.YELLOW);
                break;
        }

        doneView.setVisibility(task.isDone() ? View.VISIBLE : View.GONE);
        attachmentIcon.setVisibility((task.getAttachments() != null && !task.getAttachments().isEmpty()) ? View.VISIBLE : View.GONE);
    }
}
