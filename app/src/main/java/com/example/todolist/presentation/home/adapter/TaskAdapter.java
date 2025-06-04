package com.example.todolist.presentation.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.domain.model.Task;
import com.example.todolist.util.converter.Converters;
import com.example.todolist.util.task.BaseTaskAdapter;

import java.util.List;

public class TaskAdapter extends BaseTaskAdapter<TaskAdapter.TaskViewHolder> {

    private final OnTaskClickListener listener;

    public TaskAdapter(@NonNull List<Task> tasks, @NonNull OnTaskClickListener listener) {
        super(tasks);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        String deadlineText = holder.itemView.getContext().getString(R.string.deadline_prefix) +
                " " + Converters.formatLocalDateTimeToStringWithDayName(task.getDeadline());

        holder.deadlineView.setText(deadlineText);
        bindCommonTaskData(
                holder.itemView.getContext(),
                task,
                holder.titleView,
                holder.createdAtView,
                holder.priorityView,
                holder.doneView,
                holder.attachmentIcon
        );

        holder.itemView.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), v);
            popupMenu.inflate(R.menu.task_context_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.edit_task) {
                    listener.onEditClick(task);
                    return true;
                }
                else if (item.getItemId() == R.id.delete_task) {
                    listener.onDeleteClick(task);
                    return true;
                }
                else if (item.getItemId() == R.id.change_status) {
                    listener.onChangeStatusClick(task);
                    return true;
                }
                else if (item.getItemId() == R.id.add_attachment) {
                    listener.onAddAttachmentClick(task);
                    return true;
                }
                else if (item.getItemId() == R.id.show_attachment) {
                    listener.onShowAttachmentClick(task);
                    return true;
                }
                else if (item.getItemId() == R.id.delete_attachment) {
                    listener.onDeleteAttachmentClick(task);
                    return true;
                }
                else {
                    return false;
                }
            });
            popupMenu.show();
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(task);
            return true;
        });
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView deadlineView;
        private final TextView createdAtView;
        private final TextView priorityView;
        private final TextView doneView;
        private final ImageView attachmentIcon;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.taskTitle);
            deadlineView = itemView.findViewById(R.id.taskDeadline);
            createdAtView = itemView.findViewById(R.id.taskCreated);
            priorityView = itemView.findViewById(R.id.taskPriority);
            doneView = itemView.findViewById(R.id.taskDone);
            attachmentIcon = itemView.findViewById(R.id.attachmentIcon);
        }
    }

    public interface OnTaskClickListener {
        void onEditClick(Task task);
        void onDeleteClick(Task task);
        void onChangeStatusClick(Task task);
        void onLongClick(Task task);
        void onAddAttachmentClick(Task task);
        void onDeleteAttachmentClick(Task task);
        void onShowAttachmentClick(Task task);
    }
}