package com.example.todolist;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;
import com.example.todolist.util.TaskFormHelper;

public class EditTaskActivity extends AppCompatActivity {

    private TextView headerTextView;
    private EditText titleEditText, deadlineEditText;
    private Spinner prioritySpinner;
    private Button saveButton;
    private Task taskToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task_form);

        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId == -1) {
            Toast.makeText(this, "Błąd: brak ID zadania", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        headerTextView = findViewById(R.id.task_form_header);
        titleEditText = findViewById(R.id.task_title);
        deadlineEditText = findViewById(R.id.task_deadline);
        prioritySpinner = findViewById(R.id.task_priority);
        saveButton = findViewById(R.id.task_save_button);

        headerTextView.setText("Edytuj zadanie");

        TaskFormHelper helper = new TaskFormHelper(
                this,
                this,
                titleEditText,
                deadlineEditText,
                prioritySpinner
        );

        helper.getTaskViewModel().getTaskById(taskId).observe(this, task -> {
            if (task != null) {
                taskToEdit = task;
                titleEditText.setText(task.getTitle());
                deadlineEditText.setText(Converters.fromLocalDateTimeToStringInEditActivity(task.getDeadline()));
                prioritySpinner.setSelection(getPriorityIndex(task.getPriority().getDisplayName()));
            }

            saveButton.setOnClickListener(v -> helper.handleSave(updatedTask -> finish(), true, taskToEdit));
        });
    }

    private int getPriorityIndex(String priorityName) {
        switch (priorityName) {
            case "HIGH": return 0;
            case "MEDIUM": return 1;
            default: return 2;
        }
    }
}