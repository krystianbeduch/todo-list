package com.example.todolist;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.databinding.FragmentTaskFormBinding;
import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;
import com.example.todolist.util.TaskFormHelper;

public class EditTaskActivity extends AppCompatActivity {

    private FragmentTaskFormBinding binding;
    private Task taskToEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_task_form);
        binding = FragmentTaskFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId == -1) {
            Toast.makeText(this, "Błąd: brak ID zadania", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle("Edytuj zadanie");
        String headerText = "Edytuj zadanie";
        binding.taskFormHeader.setText(headerText);

        TaskFormHelper helper = new TaskFormHelper(
                this,
                this,
                binding.taskTitle,
                binding.taskDeadline,
                binding.taskPriority

        );

        helper.getTaskViewModel().getTaskById(taskId).observe(this, task -> {
            if (task == null ) {
                Toast.makeText(this, "Nie znaleziono zadania", Toast.LENGTH_SHORT).show();
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