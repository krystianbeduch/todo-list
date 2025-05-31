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
    private EditText titleEditText, deadlineEditText;
    private Spinner prioritySpinner;
    private Button saveButton;

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

        saveButton.setOnClickListener(v -> helper.handleSave(task -> requireActivity().getOnBackPressedDispatcher().onBackPressed(), false, null));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}