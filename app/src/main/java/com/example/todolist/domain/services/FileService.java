package com.example.todolist.domain.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class FileService {
    public static void exportTasksToCsv(Context context) {
        new Thread(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                List<Task> allTasks = db.taskDao().getAllSync();

                String filename = "task_export_" + System.currentTimeMillis() + ".csv";
                Uri fileUri = null;
                OutputStream outputStream;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
                    values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                    fileUri = context.getContentResolver().insert(uri, values);
                    assert fileUri != null;
                    outputStream = context.getContentResolver().openOutputStream(fileUri);
                }
                else {
                    // Starsze androidy
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    File file = new File(path, filename);
                    outputStream = new FileOutputStream(file);
                }

                if (outputStream != null) {
                    StringBuilder csvBuilder = new StringBuilder();
                    csvBuilder.append("ID;Tytuł;Termin;Priorytet;Status;Data dodania\n");
                    for (Task task : allTasks) {
                        csvBuilder.append(task.getId()).append(";");
                        csvBuilder.append(sanitize(task.getTitle())).append(";");
                        csvBuilder.append(Converters.fromLocalDateTimeToString(task.getDeadline())).append(";");
                        csvBuilder.append(task.getPriority().getDisplayName()).append(";");
                        csvBuilder.append(task.isDone() ? 1 : 0).append(";");
                        csvBuilder.append(Converters.fromLocalDateTimeToString(task.getCreatedAt())).append("\n");
                    }

                    outputStream.write(csvBuilder.toString().getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    outputStream.close();

                    showToast(context, "Wyeksportowano do: " + fileUri);
                    Log.i("Export tasks" , String.valueOf(fileUri));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                showToast(context, "Błąd eksportu: " + e.getMessage());
                Log.e("Error export" , Objects.requireNonNull(e.getMessage()));
            }
        }).start();
    }

    public static void importTasksFromCsv(Context context, Uri fileUri) {
        new Thread(() -> {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
                if (inputStream == null) {
                    showToast(context, "Nie udało się otworzyć pliku CSV");
                    Log.e("Import error", "Cannot open CSV file" + fileUri);
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                boolean isFirstLine = true;
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());

                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] fields = line.split(";");
                    if (fields.length < 6) {
                        continue;
                    }

                    Task task = new Task();
                    task.setTitle(fields[1]);
                    task.setDeadline(Converters.fromStringToLocalDateTime(fields[2]));
                    task.setPriority(Priority.fromDisplayName(fields[3]));
                    task.setDone(fields[4].equals("1"));
                    task.setCreatedAt(Converters.fromStringToLocalDateTime(fields[5]));
                    db.taskDao().insert(task);
                }
                reader.close();
                inputStream.close();
                showToast(context, "Import zakończony");
                Log.i("Import success" , String.valueOf(fileUri));
            }
            catch (Exception e) {
                e.printStackTrace();
                showToast(context, "Błąd importu: " + e.getMessage());
                Log.e("Error import" , Objects.requireNonNull(e.getMessage()));
            }
        }).start();
    }

    public static Uri copyFileToInternalStorage(Context context, Uri sourceUri, String filename) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
            File targetFile = new File(context.getFilesDir(), filename);
            OutputStream outputStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[1024];
            int length;
            while (true) {
                assert inputStream != null;
                if (!((length = inputStream.read(buffer)) > 0)) break;
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            return Uri.fromFile(targetFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileNameFromUri(Context context, Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        input = input.replace("\"", "\"\"");
        if (input.contains(";") || input.contains("\n")) {
            return "\"" + input + "\"";
        }
        return input;
    }

    private static void showToast(Context context, String message) {
        new Handler(Looper.getMainLooper()).post(() ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        );
    }
}
