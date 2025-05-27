package com.example.todolist.presentation.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todolist.R;
import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TaskFormFragment extends Fragment {

//    private FragmentDashboardBinding binding;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        DashboardViewModel dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);
//
//        binding = FragmentDashboardBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }

    private EditText titleEditText, deadlineEditText;
    private final Calendar selectedDateTime = Calendar.getInstance();
    private Spinner prioritySpinner;
    private Button saveButton;
    private TaskViewModel taskViewModel;
    private Task taskToEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_form, container, false);
        titleEditText = root.findViewById(R.id.task_title);
        deadlineEditText = root.findViewById(R.id.task_deadline);
        deadlineEditText.setOnClickListener(v -> showDateTimePicker());

        prioritySpinner = root.findViewById(R.id.task_priority);
        saveButton = root.findViewById(R.id.task_save_button);

        ArrayAdapter<Priority> adapter = getPriorityArrayAdapter();
        prioritySpinner.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
//        if (getArguments() != null && getArguments().containsKey("task")) {
//            taskToEdit = (Task) getArguments().getSerializable("task");
//
//            // Ustaw dane do edycji
//            titleEditText.setText(taskToEdit.getTitle());
//            deadlineEditText.setText(taskToEdit.getDeadline());
//            prioritySpinner.setSelection(getPriorityIndex(taskToEdit.getPriority()));
//            saveButton.setText("Zapisz zmiany");
//        }


        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            LocalDateTime deadline = LocalDateTime.parse(deadlineEditText.getText().toString());
            String priority = prioritySpinner.getSelectedItem().toString();

            if (!title.isEmpty()) {
                if (taskToEdit == null) {
                    // Dodawanie
                    Task newTask = new Task(title, deadline, false, Priority.valueOf(priority), LocalDateTime.now());
                    taskViewModel.insert(newTask);
                    Toast.makeText(requireContext(), "Zadanie dodane", Toast.LENGTH_SHORT).show();
                } else {
                    // Edycja
//                    taskToEdit.setTitle(title);
//                    taskToEdit.setDeadline(deadline);
//                    taskToEdit.setPriority(priority);
//                    taskViewModel.update(taskToEdit);
//                    Toast.makeText(requireContext(), "Zadanie zaktualizowane", Toast.LENGTH_SHORT).show();
                }
                requireActivity().onBackPressed(); // Powrót
            } else {
                Toast.makeText(requireContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @NonNull
    private ArrayAdapter<Priority> getPriorityArrayAdapter() {
        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Priority.values()) {
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
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
                requireContext(),
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
}