package com.example.todolist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.todolist.domain.model.SortType;
import com.example.todolist.domain.services.FileService;
import com.example.todolist.presentation.viewmodel.TaskViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todolist.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private TaskViewModel taskViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkNotificationPermission();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_task_manager, R.id.navigation_notifications, R.id.nav_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getTasksForNotification().observe(this, tasks -> {
            BadgeDrawable badge = navView.getOrCreateBadge(R.id.navigation_notifications);
            if (!tasks.isEmpty()) {
                badge.setVisible(true);
                badge.setNumber(tasks.size());
            }
            else {
                badge.setVisible(false);
            }
        });

        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_more) {
                View moreItemView = findViewById(R.id.nav_more);
                if (moreItemView != null) {
                    showMorePopup(moreItemView);
                }
                else {
                    Toast.makeText(this, "Błąd: Nie znaleziono przycisku", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            else {
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
        });

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            FileService.importTasksFromCsv(this, fileUri);
                        }
                    }
                }
        );
    }


    private void showMorePopup(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenu().add("Importuj zadania");
        popupMenu.getMenu().add("Eksportuj zadania");
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (Objects.requireNonNull(menuItem.getTitle()).toString()) {
                case "Importuj zadania":
//                    Toast.makeText(this, "Importowanie..", Toast.LENGTH_SHORT).show();
                    openFilePicker();
                    break;
                case "Eksportuj zadania":
                    FileService.exportTasksToCsv(this);
                    break;
            }
            return true;
        });

        popupMenu.show();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/csv");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Wybierz plik CSV"));
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
    }
}