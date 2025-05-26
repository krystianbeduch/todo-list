package com.example.todolist.presentation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;
import com.example.todolist.presentation.home.adapter.TaskAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

//    private FragmentHomeBinding binding;
//    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TaskViewModel taskViewModel;
    private boolean inserted = false;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(new ArrayList<>());
        recyclerView.setAdapter(taskAdapter);

//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//        homeViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
//            taskAdapter.setTasks(tasks);
//        });

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);

            if (!inserted && tasks.isEmpty()) {
                insertDummyTasks();
                inserted = true;
            }
        });

        return root;
    }

    private void insertDummyTasks() {
        taskViewModel.insert(new Task("Kupić mleko", "2025-05-22", false));
        taskViewModel.insert(new Task("Zadanie z matematyki", "2025-05-31", false));
        taskViewModel.insert(new Task("Siłownia", "2025-05-27", false));
        taskViewModel.insert(new Task("Wynieść śmieci", "2025-05-27", false));
        taskViewModel.insert(new Task("Spotkanie z zespołem", "2025-05-28", true));
        taskViewModel.insert(new Task("Zrobić zakupy", "2025-05-29", false));
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}