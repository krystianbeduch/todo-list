package com.example.todolist.util.task;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.domain.model.Task;
import com.example.todolist.util.converter.Converters;

import java.util.List;

public abstract class BaseTaskAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<Task> tasks;

    public BaseTaskAdapter(@NonNull List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTasks(@NonNull List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getItemCount() {
        return tasks != null ? tasks.size() : 0;
    }

    protected void bindCommonTaskData(@NonNull Context context,
                                      @NonNull Task task,
                                      @NonNull TextView titleView,
                                      @NonNull TextView createdAtView,
                                      @NonNull TextView priorityView,
                                      @NonNull TextView doneView,
                                      @NonNull ImageView attachmentIcon) {

        titleView.setText(task.getTitle());

        String createdAtText = context.getString(R.string.created_prefix) + " " + Converters.formatLocalDateTimeToStringWithDayName(context, task.getCreatedAt());
        createdAtView.setText(createdAtText);

        String priorityText = context.getString(R.string.priority_prefix) + " " + context.getString(task.getPriority().getStringResId());
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

        boolean hasAttachments = task.getAttachments() != null && !task.getAttachments().isEmpty();
        attachmentIcon.setVisibility(hasAttachments ? View.VISIBLE : View.GONE);
    }

    public void updateTasks(@NonNull List<Task> newTasks) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
               return tasks != null ? tasks.size() : 0;
            }

            @Override
            public int getNewListSize() {
                return newTasks.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return tasks.get(oldItemPosition).getId() == newTasks.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Task oldTask = tasks.get(oldItemPosition);
                Task newTask = newTasks.get(newItemPosition);
                return oldTask.equals(newTask);
            }
        });

        this.tasks = newTasks;
        diffResult.dispatchUpdatesTo(this);
    }
}
