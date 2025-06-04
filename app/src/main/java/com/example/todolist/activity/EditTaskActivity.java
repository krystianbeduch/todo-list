package com.example.todolist.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.databinding.FragmentTaskFormBinding;
import com.example.todolist.domain.model.enums.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.util.converter.Converters;
import com.example.todolist.util.task.TaskFormHelper;

public class EditTaskActivity extends AppCompatActivity {

    private FragmentTaskFormBinding binding;
    private Task taskToEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentTaskFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId == -1) {
            Toast.makeText(this, getString(R.string.error_no_task_id), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle(getString(R.string.edit_task_header));
        String headerText = getString(R.string.edit_task_header);
        binding.taskFormHeader.setText(headerText);

        TaskFormHelper helper = new TaskFormHelper(
                this,
                binding.taskTitle,
                binding.taskDeadline,
                binding.taskPriority
        );

        helper.getTaskViewModel().getTaskById(taskId).observe(this, task -> {
            if (task == null ) {
                Toast.makeText(this, getString(R.string.no_task_found), Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            taskToEdit = task;
            binding.taskTitle.setText(task.getTitle());
            binding.taskDeadline.setText(Converters.fromLocalDateTimeToString(task.getDeadline()));
            binding.taskPriority.setSelection(Priority.getPriorityIndex(task.getPriority()));
            binding.taskSaveButton.setOnClickListener(v -> helper.handleSave(updatedTask -> finish(), true, taskToEdit));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        taskToEdit = null;
    }
}