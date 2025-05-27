package com.example.todolist;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.todolist.data.dao.TaskDao;
import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private AppDatabase database;
    private TaskDao taskDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase.class) // baza na czas testow
                .allowMainThreadQueries() // w testach mozemy uzywac zapytac na main thread
                .build();
        taskDao = database.taskDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndGetTask() {
        Task task = new Task("Test task", LocalDateTime.now().plusDays(3), false, 1, LocalDateTime.now());
        taskDao.insert(task);

        List<Task> allTasks = taskDao.getAll();
        assertEquals(1, allTasks.size());
        assertEquals("Test task", allTasks.get(0).getTitle());
    }
}
