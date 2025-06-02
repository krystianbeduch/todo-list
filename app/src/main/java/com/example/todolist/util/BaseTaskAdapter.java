package com.example.todolist.util;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;

import java.util.List;

public abstract class BaseTaskAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<Task> tasks;

    public BaseTaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    protected void bindCommonTaskData(Task task,
                                      TextView titleView,
                                      TextView createdAtView,
                                      TextView priorityView,
                                      TextView doneView,
                                      ImageView attachmentIcon) {

        if (titleView != null) {
            titleView.setText(task.getTitle());
        }

        if (createdAtView != null) {
            String createdAtText = "Utworzono: " + Converters.formatLocalDateTimeToReadableInRecyclerView(task.getCreatedAt());
            createdAtView.setText(createdAtText);
        }

        if (priorityView != null) {
            String priorityText = "Priorytet: " + task.getPriority().getDisplayName();
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
        }

        if (doneView != null) {
            doneView.setVisibility(task.isDone() ? View.VISIBLE : View.GONE);
        }

        if (attachmentIcon != null) {
            attachmentIcon.setVisibility((
                    task.getAttachments() != null && !task.getAttachments().isEmpty()) ? View.VISIBLE : View.GONE);
        }


    }
//        titleView.setText(task.getTitle());







}
