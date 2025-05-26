//package com.example.todolist;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.todolist.ui.data.model.Task;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TaskListFragment extends Fragment {
//    private RecyclerView recyclerView;
//    private TaskAdapter taskAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_task_list, container, false);
//        recyclerView = root.findViewById(R.id.tasksRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Przykładowe dane
//        List<Task> tasks = new ArrayList<>();
//        tasks.add(new Task("Wynieść śmieci", "2025-05-27", false));
//        tasks.add(new Task("Spotkanie z zespołem", "2025-05-28", true));
//        tasks.add(new Task("Zrobić zakupy", "2025-05-29", false));
//
//        taskAdapter = new TaskAdapter(tasks);
//        recyclerView.setAdapter(taskAdapter);
//
//        return root;
//    }
//}
