package com.example.todolist.presentation.notifications.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.domain.model.Task;
import com.example.todolist.util.converter.Converters;
import com.example.todolist.util.task.BaseTaskAdapter;

import java.util.List;

public class NotificationAdapter extends BaseTaskAdapter<NotificationAdapter.NotificationTaskViewHolder> {

    private final Context context;
    public NotificationAdapter(@NonNull Context context, @NonNull List<Task> tasks) {
        super(tasks);
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_task, parent, false);
        return new NotificationTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationTaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        String notificationTypeText = task.getNotificationType().getTextToNotification(context) +
                Converters.formatLocalDateTimeToStringWithDayName(holder.itemView.getContext(), task.getDeadline());

        holder.notificationTypeView.setText(notificationTypeText);
        bindCommonTaskData(
                holder.itemView.getContext(),
                task,
                holder.titleView,
                holder.createdAtView,
                holder.priorityView,
                holder.doneView,
                holder.attachmentIcon
        );
    }

    public static class NotificationTaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView notificationTypeView;
        private final TextView titleView;
        private final TextView createdAtView;
        private final TextView priorityView;
        private final TextView doneView;
        private final ImageView attachmentIcon;

        public NotificationTaskViewHolder(View itemView) {
            super(itemView);
            notificationTypeView = itemView.findViewById(R.id.notificationType);
            titleView = itemView.findViewById(R.id.taskTitle);
            createdAtView = itemView.findViewById(R.id.taskCreated);
            priorityView = itemView.findViewById(R.id.taskPriority);
            doneView = itemView.findViewById(R.id.taskDone);
            attachmentIcon = itemView.findViewById(R.id.attachmentIcon);
        }
    }
}