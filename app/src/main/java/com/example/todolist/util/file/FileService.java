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
import com.example.todolist.util.converter.Converters;
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
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FileService {
    public static void exportTasksToCsv(Context context) {
        writeToFile(context, FileType.CSV, outputStream -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                List<Task> allTasks = db.taskDao().getAllSync();

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
            }
            catch (Exception e) {
                showToast(context, "Błąd eksportu CSV: " + e.getMessage());
                Log.e("Error CSV export" , Log.getStackTraceString(e));
            }
        });
    }

    public static void importTasksFromCsv(Context context, Uri fileUri) {
        readFromFile(context, fileUri, FileType.CSV, inputStream -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
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
            }
            catch (Exception e) {
                showToast(context, "Błąd importu CSV: " + e.getMessage());
                Log.e("Error CSV import" , Log.getStackTraceString(e));
            }
        });
    }

    public static void exportTasksToJson(Context context) {
        writeToFile(context, FileType.JSON, outputStream -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                List<Task> allTasks = db.taskDao().getAllSync();

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskTypeJsonAdapter())
                        .setPrettyPrinting()
                        .create();

                String json = gson.toJson(allTasks);

                outputStream.write(json.getBytes(StandardCharsets.UTF_8));
            }
            catch (Exception e) {
                showToast(context, "Błąd eksportu JSON: " + e.getMessage());
                Log.e("Error JSON export" , Log.getStackTraceString(e));
            }
        });
    }

    public static void importTasksFromJson(Context context, Uri fileUri) {
        readFromFile(context, fileUri, FileType.JSON, inputStream -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Task.class, new TaskTypeJsonAdapter())
                        .create();

                Type taskListType = new TypeToken<List<Task>>(){}.getType();
                List<Task> tasks = gson.fromJson(reader, taskListType);

                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                for (Task task : tasks) {
                    db.taskDao().insert(task);
                }
            }
            catch (Exception e) {
                showToast(context, "Błąd importu JSON: " + e.getMessage());
                Log.e("Error JSON import" , Log.getStackTraceString(e));
            }
        });
    }

    public static void exportTasksToXml(Context context) {
        writeToFile(context, FileType.XML, outputStream -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                List<Task> allTasks = db.taskDao().getAllSync();

                List<TaskXml> taskXmlList = allTasks.stream()
                        .map(TaskXml::new)
                        .collect(Collectors.toList());

                TaskXmlWrapper wrapper = new TaskXmlWrapper(taskXmlList);
                Serializer serializer = new Persister();
                serializer.write(wrapper, outputStream);
            }
            catch (Exception e) {
                showToast(context, "Błąd eksportu XML: " + e.getMessage());
                Log.e("Error XML export" , Log.getStackTraceString(e));
            }
        });
    }

    public static void importTasksFromXml(Context context, Uri fileUri) {
        readFromFile(context, fileUri, FileType.XML, inputStream -> {
            try {
                Serializer serializer = new Persister();
                TaskXmlWrapper wrapper = serializer.read(TaskXmlWrapper.class, inputStream);

                List<Task> tasks = wrapper.getTaskXmlList()
                        .stream()
                        .map(TaskXml::toTask)
                        .collect(Collectors.toList());

                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                for (Task task : tasks) {
                    db.taskDao().insert(task);
                }
            }
            catch (Exception e) {
                showToast(context, "Błąd importu JSON: " + e.getMessage());
                Log.e("Error JSON import" , Log.getStackTraceString(e));
            }
        });
    }

    private static OutputStream openOutputStream(Context context, String filename, FileType fileType) throws IOException {
        OutputStream outputStream;
        Uri fileUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
            values.put(MediaStore.Downloads.MIME_TYPE, fileType.getMimeType());
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            fileUri = context.getContentResolver().insert(uri, values);
            if (fileUri == null) {
                throw new IOException("Nie można utworzyć pliku " + fileType.name());
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
            throw new IOException("Nie można otworzyć strumienia do zapisu " + fileType.name());
        }
        return outputStream;
    }

    private static InputStream openInputStream(Context context, Uri fileUri, FileType fileType) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
        if (inputStream == null) {
            throw new IOException("Nie udało się otworzyć pliku " + fileType.name());
        }
        return inputStream;
    }

    private static void writeToFile(Context context, FileType fileType, Consumer<OutputStream> writer) {
        String filename = "task_export_" + System.currentTimeMillis() + fileType.getExtension();
        AsyncTask.execute(() -> {
            try (OutputStream os = openOutputStream(context, filename, fileType)) {
                writer.accept(os);
                showToast(context, "Wyeksportowano " + fileType.name());
            }
            catch (Exception e) {
                showToast(context, "Błąd eksportu " + fileType.name() + ": " + e.getMessage());
                Log.e("Error export" + fileType.name(), Log.getStackTraceString(e));
            }
        });
    }

    private static void readFromFile(Context context, Uri fileUri, FileType fileType, Consumer<InputStream> reader) {
        AsyncTask.execute(() -> {
            try (InputStream is = openInputStream(context, fileUri, fileType)) {
                reader.accept(is);
                showToast(context, "Import " + fileType.name() + " zakończony");
            }
            catch (Exception e) {
                showToast(context, "Błąd importu: " + e.getMessage());
                Log.e("Error import", Log.getStackTraceString(e));
            }
        });
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
            Log.e("IOException: ", Log.getStackTraceString(e));
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

    public static String getFileNameFromFilePicker(Context context, Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
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
            if (result != null) {
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result;
    }

    public static boolean isSupportedExtensionForFileType(FileType fileType, String filename) {
        return filename != null && filename.toLowerCase().endsWith(fileType.getExtension());
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
            Log.e("Exception: ", Log.getStackTraceString(e));
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