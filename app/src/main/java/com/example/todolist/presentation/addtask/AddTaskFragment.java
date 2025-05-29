package com.example.todolist.presentation.addtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.todolist.R;
//import com.example.todolist.domain.model.SharedTaskViewModel;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;
import com.example.todolist.util.TaskFormHelper;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {

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

    private TextView headerTextView;
    private EditText titleEditText, deadlineEditText;
    private final Calendar selectedDateTime = Calendar.getInstance();
    private Spinner prioritySpinner;
    private Button saveButton;
    private TaskViewModel taskViewModel;
//    private SharedTaskViewModel sharedTaskViewModel;
    private Task taskToEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_form, container, false);

        titleEditText = root.findViewById(R.id.task_title);
        deadlineEditText = root.findViewById(R.id.task_deadline);
        prioritySpinner = root.findViewById(R.id.task_priority);
        saveButton = root.findViewById(R.id.task_save_button);

        TaskFormHelper helper = new TaskFormHelper(
                requireContext(),
                getViewLifecycleOwner(),
                titleEditText,
                deadlineEditText,
                prioritySpinner
        );

//        deadlineEditText.setOnClickListener(v -> showDateTimePicker());
//        ArrayAdapter<Priority> adapter = getPriorityArrayAdapter();
//        prioritySpinner.setAdapter(adapter);
//        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

//        saveButton.setOnClickListener(v -> {
//            String title = titleEditText.getText().toString().trim();
//            String deadlineText = deadlineEditText.getText().toString().trim();
//            String priority = prioritySpinner.getSelectedItem().toString();
//
//            if (title.isEmpty() || deadlineText.isEmpty()) {
//                Toast.makeText(requireContext(), "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            LocalDateTime deadline;
//            try {
//                deadline = Converters.fromTaskFormDeadlineToLocalDateTime(deadlineText);
//            }
//            catch (DateTimeParseException e) {
//                Toast.makeText(requireContext(), "Nieprawdiłowy format daty. Użyj: dd.MM.yyyy HH:mm", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if (!deadline.isAfter(LocalDateTime.now())) {
//                Toast.makeText(requireContext(), "Termin musi być w przyszłości", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            Task newTask = new Task(title, deadline, false, Priority.valueOf(priority), LocalDateTime.now());
//            taskViewModel.insert(newTask);
//            Toast.makeText(requireContext(), "Zadanie dodane", Toast.LENGTH_SHORT).show();
//            requireActivity().getOnBackPressedDispatcher().onBackPressed(); // Powrót
//        });

        saveButton.setOnClickListener(v -> helper.handleSave(task -> requireActivity().getOnBackPressedDispatcher().onBackPressed(), false, null));
        return root;
    }

//    @NonNull
//    private ArrayAdapter<Priority> getPriorityArrayAdapter() {
//        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Priority.values()) {
//            @NonNull
//            @Override
//            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                ((TextView) view).setText(Objects.requireNonNull(getItem(position)).getDisplayName());
//                return view;
//            }
//
//            @Override
//            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                ((TextView) view).setText(Objects.requireNonNull(getItem(position)).getDisplayName());
//                return view;
//            }
//        };
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        return adapter;
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

//    private void showDateTimePicker() {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                requireContext(),
//                (view, year, month, dayOfMonth) -> {
//                    selectedDateTime.set(Calendar.YEAR, year);
//                    selectedDateTime.set(Calendar.MONTH, month);
//                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                    showTimePicker();
//                },
//                selectedDateTime.get(Calendar.YEAR),
//                selectedDateTime.get(Calendar.MONTH),
//                selectedDateTime.get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//    }
//
//    private void showTimePicker() {
//        TimePickerDialog timePickerDialog = new TimePickerDialog(
//                requireContext(),
//                (view, hourOfDay, minute) -> {
//                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    selectedDateTime.set(Calendar.MINUTE, minute);
//                    updateDeadlineText();
//                },
//                selectedDateTime.get(Calendar.HOUR_OF_DAY),
//                selectedDateTime.get(Calendar.MINUTE),
//                true
//        );
//        timePickerDialog.show();
//    }
//
//    private void updateDeadlineText() {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
//        deadlineEditText.setText(sdf.format(selectedDateTime.getTime()));
//    }
}