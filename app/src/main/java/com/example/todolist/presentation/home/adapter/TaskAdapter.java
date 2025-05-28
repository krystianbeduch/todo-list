package com.example.todolist.presentation.home.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.presentation.services.Converters;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final OnTaskClickListener listener;

    private List<Task> tasks;

    public interface OnTaskClickListener {
        void onEditClick(Task task);
        void onDeleteClick(Task task);
        void onChangeStatusClick(Task task);
        void onLongClick(Task task);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView deadlineView;
        private final TextView createdAtView;
        private final TextView priorityView;
        private final TextView doneView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.taskTitle);
            deadlineView = itemView.findViewById(R.id.taskDeadline);
            createdAtView = itemView.findViewById(R.id.taskCreated);
            priorityView = itemView.findViewById(R.id.taskPriority);
            doneView = itemView.findViewById(R.id.taskDone);
        }
    }

    public TaskAdapter(List<Task> tasks, OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.titleView.setText(task.getTitle());
        holder.deadlineView.setText("Termin: " + Converters.formatLocalDateTimeToReadableInRecyclerView(task.getDeadline()));
        holder.createdAtView.setText("Utworzono: " +  Converters.formatLocalDateTimeToReadableInRecyclerView(task.getCreatedAt()));

        holder.priorityView.setText("Priorytet: " + task.getPriority().getDisplayName());
        if (task.getPriority() == Priority.HIGH) {
            holder.priorityView.setTextColor(Color.RED);
        }
        else if (task.getPriority() == Priority.MEDIUM) {
            holder.priorityView.setTextColor(Color.rgb(255, 165, 0));
        }
        else {
            holder.priorityView.setTextColor(Color.YELLOW);
        }

        if (task.isDone()) {
            holder.doneView.setVisibility(View.VISIBLE);
        }
        else {
            holder.doneView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
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

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
