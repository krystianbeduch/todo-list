package com.example.todolist.presentation.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.EditTaskActivity;
import com.example.todolist.R;
import com.example.todolist.domain.model.Attachment;
import com.example.todolist.domain.model.Priority;
//import com.example.todolist.domain.model.SharedTaskViewModel;
import com.example.todolist.domain.model.SortType;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.model.TaskViewModel;
import com.example.todolist.domain.services.FileService;
import com.example.todolist.presentation.home.adapter.TaskAdapter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

//    private FragmentHomeBinding binding;
//    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private TaskViewModel taskViewModel;
//    private SharedTaskViewModel sharedViewModel;
    private boolean inserted = false;
    private static final int PICK_ATTACHMENT_REQUEST_CODE = 1001;
    private Task currentAttachmentTask = null;
    private ActivityResultLauncher<Intent> attachmentPickerLauncher;

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

//    @Override
//    public void onCreateOptionsMenu


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        attachmentPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri sourceUri = result.getData().getData();
                        assert sourceUri != null;
                        String filename = FileService.getFileNameFromUri(getContext(), sourceUri);
                        Uri localUri = FileService.copyFileToInternalStorage(requireContext(), sourceUri, filename);

                        if (localUri != null && currentAttachmentTask != null) {
                            Log.d("Attachment", "Selected file: " + localUri);
                            Attachment attachment = new Attachment(
                                    currentAttachmentTask.getId(),
                                    filename,
                                    localUri.toString()
                            );
                            taskViewModel.addAttachmentToTask(attachment);
                        }
                    }
                }
        );

//        Spinner sortSpinner = root.findViewById(R.id.sortSpinner);
//        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                SortType selected = SortType.CREATED_DATE;
//                switch (position) {
//                    case 1: selected = SortType.TITLE; break;
//                    case 2: selected = SortType.DEADLINE; break;
//                    case 3: selected = SortType.PRIORITY; break;
//                    case 4: selected = SortType.STATUS; break;
//                }
//                taskViewModel.setSortType(selected);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });

        taskAdapter = new TaskAdapter(new ArrayList<>(), new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onEditClick(Task task) {
                Log.i("Task", "Edit: " + task.getId() + "." + task.getTitle());
//                Bundle bundle = new Bundle();
//                bundle.putInt("task_id", task.getId());
//                bundle.putString("task_title", task.getTitle());
//                bundle.putString("task_deadline", task.getDeadline().toString()); // LocalDateTime -> ISO String
//                bundle.putBoolean("task_isDone", task.isDone());
//                bundle.putString("task_priority", task.getPriority().name());
//                bundle.putString("task_createdAt", task.getCreatedAt().toString());

                Intent intent = new Intent(getContext(), EditTaskActivity.class);
                intent.putExtra("taskId", task.getId());
                startActivity(intent);;

//                TaskFormFragment taskFormFragment = new TaskFormFragment();
//                taskFormFragment.setArguments(bundle);
//                requireActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment_activity_main, taskFormFragment)
//                        .addToBackStack(null)
//                        .commit();
//

//                NavController navController = Navigation.findNavController(requireView());
//                navController.navigate(R.id.fragment_task_form, bundle);
//                NavController navController = NavHostFragment.findNavController(HomeFragment.this);
//                navController.navigate(R.id.navigation_task_manager, bundle,
//                        new NavOptions.Builder()
//                                .setPopUpTo(R.id.navigation_home, false)
//                                .build()
//                );
//
//                TaskFormFragment fragment = new TaskFormFragment();
//                fragment.setArguments(bundle);


//                sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedTaskViewModel.class);
//                sharedViewModel.selectTask(task);
//                ((MainActivity) requireActivity()).switchToMenuItem(R.id.navigation_task_manager);

                // Przełącz na zakładkę "TaskForm"
//                BottomNavigationView nav = requireActivity().findViewById(R.id.nav_view);
//                nav.setSelectedItemId(R.id.navigation_task_manager);

//                requireActivity().setTitle("Edytuj zadanie");

            }

            @Override
            public void onDeleteClick(Task task) {
                Log.i("Delete task", task.getId() + ". " + task.getTitle());
                taskViewModel.delete(task);
            }

            @Override
            public void onChangeStatusClick(Task task) {
                Log.i("Change task status", task.getId() + "." + task.getTitle());
                taskViewModel.changeStatus(task);
            }

            @Override
            public void onLongClick(Task task) {
                Log.i("Change task status", task.getId() + "." + task.getTitle());
                taskViewModel.changeStatus(task);
            }

            @Override
            public void onAddAttachmentClick(Task task) {
                Log.i("Add attachment", task.getId() + "." + task.getTitle());
                currentAttachmentTask = task;

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*", "application/pdf"});
                attachmentPickerLauncher.launch(intent);
            }

            @Override
            public void onDeleteAttachmentClick(Task task) {

            }

            @Override
            public void onShowAttachmentClick(Task task) {

            }


        });
        recyclerView.setAdapter(taskAdapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
//        taskViewModel.deleteAll(this::insertDummyTasks);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);

            if (!inserted && tasks.isEmpty()) {
                insertDummyTasks();
                inserted = true;
            }
        });

//        taskViewModel.getSortedTasks().observe(getViewLifecycleOwner(), tasks -> {
//            taskAdapter.setTasks(tasks);
//        });


//        taskViewModel.getSortedTasks().observe(getViewLifecycleOwner(), tasks -> {
//
//        });
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
        MenuItem item = menu.findItem(R.id.sort_spinner_item);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                SortType selected = SortType.CREATED_DATE;
//                switch (position) {
//                    case 1: selected = SortType.TITLE; break;
//                    case 2: selected = SortType.DEADLINE; break;
//                    case 3: selected = SortType.PRIORITY; break;
//                    case 4: selected = SortType.STATUS; break;
//                }
                SortType selected = SortType.values()[position];
                List<Task> currentTasks = taskViewModel.getTasks().getValue();
//                Log.i("ct", currentTasks.get(0).getTitle());
                if (currentTasks != null) {
                    taskViewModel.sortTasks(new ArrayList<>(currentTasks), selected);
                }

//                taskViewModel.loadTasksBySort(selected);
//                taskViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
//                    taskAdapter.setTasks(tasks);
//                });
//                taskViewModel.setSortType(selected);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_ATTACHMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (currentAttachmentTask != null) {
                Uri attachmentUri = data.getData();
                assert attachmentUri != null;
                requireContext().getContentResolver().takePersistableUriPermission(
                        attachmentUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
                Log.d("Attachment", "Attachment added: " + attachmentUri);
            }
        }
    }
}