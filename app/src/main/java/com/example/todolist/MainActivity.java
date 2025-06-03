package com.example.todolist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.todolist.domain.model.FileType;
import com.example.todolist.presentation.viewmodel.TaskViewModel;
import com.example.todolist.util.file.FileService;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.todolist.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private FileType currentImportFileType;
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
                R.id.navigation_home, R.id.navigation_task_manager, R.id.navigation_notifications)
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
                            String fileName = getFileName(fileUri);
                            if (fileName != null && isSupportedExtensionForCurrentFileType(fileName)) {
                                taskViewModel.importTasksFromFile(this, fileUri, currentImportFileType);
                            }
                            else {
                                Toast.makeText(this, "Błędny format pliku", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            assert result != null;
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private boolean isSupportedExtensionForCurrentFileType(String fileName) {
        if (currentImportFileType == null) {
            return false;
        }
        String lower = fileName.toLowerCase();
        switch (currentImportFileType) {
            case CSV:
                return lower.endsWith(".csv");
            case JSON:
                return lower.endsWith(".json");
            case XML:
                return lower.endsWith(".xml");
            default:
                return false;
        }
    }


    private void showMorePopup(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.bottom_nav_more, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.menu_import) {
                showFormatChooser(true);
            }
            else if (id == R.id.menu_export) {
                showFormatChooser(false);
            }
            return true;
        });

        popupMenu.show();
    }

    private void showFormatChooser(boolean isImport) {
        String[] formats = Arrays.stream(FileType.values())
                .map(Enum::name)
                .toArray(String[]::new);

        new AlertDialog.Builder(this)
                .setTitle("Wybierz format pliku")
                .setItems(formats, (dialog, which) -> {
                    FileType selectedFormat = FileType.values()[which];
                    if (isImport) {
                        currentImportFileType = FileType.valueOf(selectedFormat.name());
                        openFilePicker(currentImportFileType);
                    }
                    else {
                        exportTasks(FileType.valueOf(selectedFormat.name()));
                    }
                })
                .show();
    }

    private void openFilePicker(FileType fileType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(FileService.getMimeTypeForFormat(fileType));
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Wybierz plik " + fileType.name()));
    }

    private void exportTasks(FileType fileType) {
        taskViewModel.exportTasksToFile(this, fileType);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}