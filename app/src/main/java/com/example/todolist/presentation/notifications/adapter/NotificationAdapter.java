package com.example.todolist.presentation.notifications.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;
import com.example.todolist.util.TaskViewBinder;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationTaskViewHolder> {
    private List<Task> tasks;

    public NotificationAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
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

        String notificationTypeText;
        switch (task.getNotificationType()) {
            case OVERDUE:
                notificationTypeText = "Minął termin zadania\n";
                break;
            case UPCOMING:
                notificationTypeText = "Zbliżający się termin zadania\n";
                break;
            default:
                notificationTypeText = "";
        }
        notificationTypeText += Converters.formatLocalDateTimeToReadableInRecyclerView(task.getDeadline());

        holder.notificationTypeView.setText(notificationTypeText);
        TaskViewBinder.bindCommonTaskData(
                task,
                holder.titleView,
                holder.createdAtView,
                holder.priorityView,
                holder.doneView,
                holder.attachmentIcon
        );
//        String createdAtText = "Utworzono: " +  Converters.formatLocalDateTimeToReadableInRecyclerView(task.getCreatedAt());
//        String priorityText = "Priorytet: " + task.getPriority().getDisplayName();
//

//        holder.titleView.setText(task.getTitle()); //
//        holder.createdAtView.setText(createdAtText); //
//        holder.priorityView.setText(priorityText); //
//
//        switch (task.getPriority()) { //
//            case HIGH:
//                holder.priorityView.setTextColor(Color.RED);
//                break;
//            case MEDIUM:
//                holder.priorityView.setTextColor(Color.rgb(255, 165, 0));
//                break;
//            case LOW:
//                holder.priorityView.setTextColor(Color.YELLOW);
//                break;
//        }
//
//        holder.doneView.setVisibility(task.isDone() ? View.VISIBLE : View.GONE); //
//        holder.attachmentIcon.setVisibility((task.getAttachments() != null && !task.getAttachments().isEmpty()) ? View.VISIBLE : View.GONE); //

    }

    @Override
    public int getItemCount() {
        return tasks.size();
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
