package com.example.todolist.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;
import com.example.todolist.domain.services.Converters;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TaskFormHelper {

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final Calendar selectedDateTime = Calendar.getInstance();
    private final TaskViewModel taskViewModel;

    private final EditText titleEditText;
    private final EditText deadlineEditText;
    private final Spinner prioritySpinner;

    public interface OnTaskSaveCallback {
        void onSuccess(Task task);
    }

    public TaskFormHelper(Context context, LifecycleOwner lifecycleOwner, EditText titleEditText, EditText deadlineEditText, Spinner prioritySpinner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.titleEditText = titleEditText;
        this.deadlineEditText = deadlineEditText;
        this.prioritySpinner = prioritySpinner;

        this.taskViewModel =
                new ViewModelProvider((ViewModelStoreOwner) context).get(TaskViewModel.class);

        setupDeadlinePicker();
        setupPrioritySpinner();

    }

    private void setupDeadlinePicker () {
        deadlineEditText.setOnClickListener(v -> showDateTimePicker());
    }

    private void setupPrioritySpinner() {
        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, Priority.values()) {
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
        prioritySpinner.setAdapter(adapter);
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
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
                context,
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

    public void handleSave(OnTaskSaveCallback callback, boolean isEditMode, Task existingTask) {
        String title = titleEditText.getText().toString().trim();
        String deadlineText = deadlineEditText.getText().toString().trim();
        String priority = prioritySpinner.getSelectedItem().toString();

        if (title.isEmpty() || deadlineText.isEmpty()) {
            Toast.makeText(context, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDateTime deadline;
        try {
            deadline = Converters.fromStringToLocalDateTime(deadlineText);
        }
        catch (DateTimeParseException e) {
            Toast.makeText(context, "Nieprawdiłowy format daty. Użyj: dd.MM.yyyy HH:mm", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!deadline.isAfter(LocalDateTime.now())) {
            Toast.makeText(context, "Termin musi być w przyszłości", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode && existingTask != null) {
            existingTask.setTitle(title);
            existingTask.setDeadline(deadline);
            existingTask.setPriority(Priority.valueOf(priority));
            taskViewModel.update(existingTask);
            Toast.makeText(context, "Zadanie zaktualizowane", Toast.LENGTH_SHORT).show();
            callback.onSuccess(existingTask);
        }
        else {
            Task newTask = new Task(title, deadline, false, Priority.valueOf(priority), LocalDateTime.now());
            taskViewModel.insert(newTask);
            Toast.makeText(context, "Zadanie dodane", Toast.LENGTH_SHORT).show();
            callback.onSuccess(existingTask);
        }
    }

    public TaskViewModel getTaskViewModel() {
        return taskViewModel;
    }
}
