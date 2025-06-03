package com.example.todolist.util.file;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.FileType;
import com.example.todolist.domain.model.Priority;
import com.example.todolist.domain.model.Task;
import com.example.todolist.domain.services.Converters;
import com.example.todolist.util.file.xml.TaskXml;
import com.example.todolist.util.file.xml.TaskXmlWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileService {
    public static void exportTasksToCsv(Context context) {
        AsyncTask.execute(() -> {
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
                    if (fileUri == null) {
                        showToast(context, "Błąd: nie można utworzyć pliku");
                        return;
                    }
                    outputStream = context.getContentResolver().openOutputStream(fileUri);
                }
                else {
                    // Starsze androidy
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    File file = new File(path, filename);
                    outputStream = new FileOutputStream(file);
                }

                if (outputStream == null) {
                    showToast(context, "Błąd: nie można otworzyć strumienia");
                    return;
                }

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
            catch (Exception e) {
                e.printStackTrace();
                showToast(context, "Błąd eksportu: " + e.getMessage());
                Log.e("Error export" , e.getMessage());
            }
        });
    }

    public static void importTasksFromCsv(Context context, Uri fileUri) {
        AsyncTask.execute(() -> {
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
                Log.e("Error import" , e.getMessage());
            }
        });
    }

    public static void exportTasksToJson(Context context) {
        AsyncTask.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                List<Task> allTasks = db.taskDao().getAllSync();

                String filename = "task_export_" + System.currentTimeMillis() + ".json";
                Uri fileUri = null;
                OutputStream outputStream;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
                    values.put(MediaStore.Downloads.MIME_TYPE, "application/json");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                    fileUri = context.getContentResolver().insert(uri, values);
                    if (fileUri == null) {
                        showToast(context, "Błąd: nie można utworzyć pliku JSON");
                        return;
                    }
                    outputStream = context.getContentResolver().openOutputStream(fileUri);
                }
                else {
                    // Starsze androidy
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    File file = new File(path, filename);
                    outputStream = new FileOutputStream(file);
                }

                if (outputStream == null) {
                    showToast(context, "Błąd: nie można otworzyć strumienia");
                    return;
                }

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskTypeJsonAdapter())
                        .setPrettyPrinting()
                        .create();

                String json = gson.toJson(allTasks);

//                StringBuilder csvBuilder = new StringBuilder();
//                csvBuilder.append("ID;Tytuł;Termin;Priorytet;Status;Data dodania\n");
//                for (Task task : allTasks) {
//                    csvBuilder.append(task.getId()).append(";");
//                    csvBuilder.append(sanitize(task.getTitle())).append(";");
//                    csvBuilder.append(Converters.fromLocalDateTimeToString(task.getDeadline())).append(";");
//                    csvBuilder.append(task.getPriority().getDisplayName()).append(";");
//                    csvBuilder.append(task.isDone() ? 1 : 0).append(";");
//                    csvBuilder.append(Converters.fromLocalDateTimeToString(task.getCreatedAt())).append("\n");
//                }

                outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                showToast(context, "Wyeksportowano JSON do: " + fileUri);
                Log.i("Export tasks to JSON" , String.valueOf(fileUri));
            }
            catch (Exception e) {
                e.printStackTrace();
                showToast(context, "Błąd eksportu JSON: " + e.getMessage());
                Log.e("Error JSON export" , e.getMessage());
            }
        });
    }

    public static void importTasksFromJson(Context context, Uri fileUri) {
        AsyncTask.execute(() -> {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
                if (inputStream == null) {
                    showToast(context, "Nie udało się otworzyć pliku JSON");
                    Log.e("Import error", "Cannot open JSON file" + fileUri);
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskTypeJsonAdapter())
                        .create();

                Type taskListType = new TypeToken<List<Task>>(){}.getType();
                List<Task> tasks = gson.fromJson(reader, taskListType);

                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                for (Task task : tasks) {
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
                Log.e("Error import" , e.getMessage());
            }
        });
    }

    public static void exportTasksToXml(Context context) {
        AsyncTask.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                List<Task> allTasks = db.taskDao().getAllSync();

                String filename = "task_export_" + System.currentTimeMillis() + ".xml";
                Uri fileUri = null;
                OutputStream outputStream;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
                    values.put(MediaStore.Downloads.MIME_TYPE, "application/xml");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                    Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                    fileUri = context.getContentResolver().insert(uri, values);
                    if (fileUri == null) {
                        showToast(context, "Błąd: nie można utworzyć pliku XML");
                        return;
                    }
                    outputStream = context.getContentResolver().openOutputStream(fileUri);
                }
                else {
                    // Starsze androidy
                    String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    File file = new File(path, filename);
                    outputStream = new FileOutputStream(file);
                }

                if (outputStream == null) {
                    showToast(context, "Błąd: nie można otworzyć strumienia");
                    return;
                }

                List<TaskXml> taskXmlList = allTasks.stream()
                        .map(TaskXml::new)
                        .collect(Collectors.toList());

                TaskXmlWrapper wrapper = new TaskXmlWrapper(taskXmlList);
                Serializer serializer = new Persister();
                serializer.write(wrapper, outputStream);

                outputStream.flush();
                outputStream.close();

                showToast(context, "Wyeksportowano XML do: " + fileUri);
                Log.i("Export tasks to JSON" , String.valueOf(fileUri));
            }
            catch (Exception e) {
                e.printStackTrace();
                showToast(context, "Błąd eksportu XML: " + e.getMessage());
                Log.e("Error XML export" , e.getMessage());
            }
        });
    }

    public static void importTasksFromXml(Context context, Uri fileUri) {
        AsyncTask.execute(() -> {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
                if (inputStream == null) {
                    showToast(context, "Nie udało się otworzyć pliku XML");
                    Log.e("Import error", "Cannot open XML file" + fileUri);
                    return;
                }

                Serializer serializer = new Persister();
                TaskXmlWrapper wrapper = serializer.read(TaskXmlWrapper.class, inputStream);

                List<TaskXml> taskXmlList = wrapper.getTaskXmlList();
                List<Task> tasks = new ArrayList<>();
                for (TaskXml taskXml : taskXmlList) {
                    tasks.add(taskXml.toTask());
                }

                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                for (Task task : tasks) {
                    db.taskDao().insert(task);
                }

                inputStream.close();
                showToast(context, "Import XML zakończony");
                Log.i("Import XML success" , String.valueOf(fileUri));
            }
            catch (Exception e) {
                e.printStackTrace();
                showToast(context, "Błąd importu XML: " + e.getMessage());
                Log.e("Error XML import" , e.getMessage());
            }
        });
    }

    public static String getMimeTypeForFormat(FileType fileType) {
        switch (fileType) {
            case CSV:
                return "text/csv";
            case JSON:
                return "application/json";
            case XML:
                return "application/xml";
            default:
                return "*/*";
        }
    }

    public static Uri copyFileToInternalStorage(Context context, Uri sourceUri, String filename) {
        String filenameWithoutExtension;
        String extension;
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex != -1) {
            filenameWithoutExtension = filename.substring(0, dotIndex);
            extension = filename.substring(dotIndex);
        }
        else {
            filenameWithoutExtension = filename;
            extension = "";
        }
        String filenameWithTimestamp = filenameWithoutExtension + "_" + System.currentTimeMillis() + extension;
        File targetFile = new File(context.getFilesDir(), filenameWithTimestamp);
        try (InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
             OutputStream outputStream = new FileOutputStream(targetFile)) {

            if (inputStream == null) {
                throw new IOException("Nie można odtworzyć InputStream z URI");
            }

            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != - 1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            return FileProvider.getUriForFile(context, "com.example.todolist.fileprovider", targetFile);
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

    public static boolean deleteFileFromInternalStorage(Context context, String filePath) {
        try {
            Uri fileUri = Uri.parse(filePath);
            String localFilePath =  fileUri.getPath();
            if (localFilePath == null || !localFilePath.contains("/files")) {
                return false;
            }

            String filename = localFilePath.substring(localFilePath.indexOf("/files/") + "/files/".length());
            File file = new File(context.getFilesDir(), filename);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
