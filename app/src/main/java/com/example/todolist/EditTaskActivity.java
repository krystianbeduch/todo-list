package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;
import com.example.todolist.presentation.services.Converters;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class EditTaskActivity extends AppCompatActivity {

    private TextView headerTextView;
    private EditText titleEditText, deadlineEditText;
    private final Calendar selectedDateTime = Calendar.getInstance();
    private Spinner prioritySpinner;
    private Button saveButton;
    private TaskViewModel taskViewModel;
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
        deadlineEditText.setOnClickListener(v -> showDateTimePicker());
        ArrayAdapter<Priority> adapter = getPriorityArrayAdapter();
        prioritySpinner.setAdapter(adapter);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.getTaskById(taskId).observe(this, task -> {
            if (task != null) {
                taskToEdit = task;
                titleEditText.setText(task.getTitle());
                deadlineEditText.setText(Converters.fromLocalDateTimeToStringInEditActivity(task.getDeadline()));
                prioritySpinner.setSelection(getPriorityIndex(task.getPriority().getDisplayName()));
            }
        });

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String deadlineText = deadlineEditText.getText().toString().trim();
            String priority = prioritySpinner.getSelectedItem().toString();

            if (title.isEmpty() || deadlineText.isEmpty()) {
                Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
                return;
            }

            LocalDateTime deadline;
            try {
                deadline = Converters.fromTaskFormDeadlineToLocalDateTime(deadlineText);
            } catch (DateTimeParseException e) {
                Toast.makeText(this, "Nieprawdiłowy format daty. Użyj: dd.MM.yyyy HH:mm", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!deadline.isAfter(LocalDateTime.now())) {
                Toast.makeText(this, "Termin musi być w przyszłości", Toast.LENGTH_SHORT).show();
                return;
            }

            taskToEdit.setTitle(title);
            taskToEdit.setDeadline(deadline);
            taskToEdit.setPriority(Priority.valueOf(priority));
            taskViewModel.update(taskToEdit);
            Toast.makeText(this, "Zadanie zaktualizowane", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    showTimePicker();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateDeadlineText();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateDeadlineText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        deadlineEditText.setText(sdf.format(selectedDateTime.getTime()));
    }

    private int getPriorityIndex(String priorityName) {
        switch (priorityName) {
            case "HIGH": return 0;
            case "MEDIUM": return 1;
            default: return 2;
        }
    }

    @NonNull
    private ArrayAdapter<Priority> getPriorityArrayAdapter() {
        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Priority.values()) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ((TextView) view).setText(Objects.requireNonNull(getItem(position)).getDisplayName());
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setText(Objects.requireNonNull(getItem(position)).getDisplayName());
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

}