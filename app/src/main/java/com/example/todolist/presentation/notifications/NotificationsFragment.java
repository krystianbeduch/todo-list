package com.example.todolist.presentation.notifications;

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
import com.example.todolist.databinding.FragmentNotificationsBinding;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;
import com.example.todolist.presentation.notifications.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private TaskViewModel taskViewModel;


//    private NotificationsViewModel viewModel;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        NotificationsViewModel notificationsViewModel =
//                new ViewModelProvider(this).get(NotificationsViewModel.class);
//
//        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
////        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
//
//        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        recyclerView = root.findViewById(R.id.notificationsRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        tasksWithNotifications =
//
//        return binding.getRoot();
//    }

//    }
    @Override
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Log.d("NotificationFragment", "onCreta wykonane");



        recyclerView = root.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationAdapter = new NotificationAdapter(new ArrayList<>());
//        notificationAdapter = new TaskAdapter(new ArrayList<>(), new TaskAdapter.OnTaskClickListener() {
//            @Override
//            public void onEditClick(Task task) {
//
//            }
//
//            @Override
//            public void onDeleteClick(Task task) {
//
//            }
//
//            @Override
//            public void onChangeStatusClick(Task task) {
//
//            }
//
//            @Override
//            public void onLongClick(Task task) {
//
//            }
//
//            @Override
//            public void onAddAttachmentClick(Task task) {
//
//            }
//
//            @Override
//            public void onDeleteAttachmentClick(Task task) {
//
//            }
//
//            @Override
//            public void onShowAttachmentClick(Task task) {
//
//            }
//        });

        recyclerView.setAdapter(notificationAdapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        taskViewModel.getTasksForNotification().observe(getViewLifecycleOwner(), tasks -> {
            notificationAdapter.setTasks(tasks);
        });

        return root;

//        NotificationUtils.createNotificationChannel(requireContext());

//        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
//
//        viewModel.getUpcomingTasks().observe(getViewLifecycleOwner(), this::handleTasks);
    }

    private void handleTasks(List<Task> tasks) {
        if (tasks == null) {
            return;
        }

//        LocalDateTime now = LocalDateTime.now();

        for (Task task : tasks) {
//            if (task.getDeadline() != null &&
//                    (task.getDeadline().isBefore(now) || task.getDeadline().isEqual(now)) && !task.isDone()) {
//                NotificationUtils.showTaskNotification(requireContext(), task);
//            }
        }
    }

//    @Override
//    public void onViewCreated(Bundle savedInstanceState) {
//        super.onViewCreated(view);
////        requestNotificationPermission();
//        NotificationUtils.createNotificationChannel(requireContext());
//
//        NotificationsViewModel viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
//
//        viewModel.getAllTasks().observe(this, tasks -> {
//            if (tasks == null) {
//                return;
//            }
//
//            LocalDateTime now = LocalDateTime.now();
//
//            for (Task task : tasks) {
//                if (task.getDeadline() != null &&
//                        (task.getDeadline().isBefore(now) || task.getDeadline().isEqual(now)) && !task.isDone()) {
//                    NotificationUtils.showTaskNotification(requireContext(), task);
//                }
//            }
//        });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    private void requestNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
//                    != PackageManager.PERMISSION_GRANTED) {
//
//                requestPermissions(
//                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
//                        1001
//                );
//            }
//        }
//    }
}