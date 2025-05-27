package com.example.todolist.presentation.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;
import com.example.todolist.presentation.home.adapter.TaskAdapter;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

        taskAdapter = new TaskAdapter(new ArrayList<>(), new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onEditClick(Task task) {
                Log.i("Task", "Edit: " + task.getId() + "." + task.getTitle());
            }

            @Override
            public void onDeleteClick(Task task) {
                Log.i("Task", "Delete: " + task.getId() + ". " + task.getTitle());
                taskViewModel.delete(task);
            }

            @Override
            public void onChangeStatusClick(Task task) {
                Log.i("Task", "Change status: " + task.getId() + "." + task.getTitle());
                taskViewModel.changeStatus(task);
            }

            @Override
            public void onLongClick(Task task) {
                taskViewModel.changeStatus(task);
            }
        });
        recyclerView.setAdapter(taskAdapter);

//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//        homeViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
//            taskAdapter.setTasks(tasks);
//        });

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.deleteAll(this::insertDummyTasks);

        taskViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);

//            if (!inserted && tasks.isEmpty()) {
//                insertDummyTasks();
//                inserted = true;
//            }
        });

        return root;
    }

    private void insertDummyTasks() {
        taskViewModel.insert(new Task("Kupić mleko",
                LocalDateTime.now().plusDays(5).with(LocalTime.of(12, 0)), true, Priority.HIGH, LocalDateTime.now()));
        taskViewModel.insert(new Task("Zadanie z matematyki",
                LocalDateTime.now().plusDays(3).with(LocalTime.of(14, 30)), false, Priority.MEDIUM, LocalDateTime.now()));
        taskViewModel.insert(new Task("Siłownia",
                LocalDateTime.now().plusDays(15).with(LocalTime.of(7, 25)), false, Priority.MEDIUM, LocalDateTime.now()));
        taskViewModel.insert(new Task("Wynieść śmieci",
                LocalDateTime.now().plusDays(25).with(LocalTime.of(9, 50)), false, Priority.LOW, LocalDateTime.now()));
        taskViewModel.insert(new Task("Spotkanie z zespołem",
                LocalDateTime.now().plusDays(2).with(LocalTime.of(18, 0)), true, Priority.LOW, LocalDateTime.now()));
        taskViewModel.insert(new Task("Zrobić zakupy",
                LocalDateTime.now().with(LocalTime.of(12, 0)), false, Priority.HIGH, LocalDateTime.now()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}