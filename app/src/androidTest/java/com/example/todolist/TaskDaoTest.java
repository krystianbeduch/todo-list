package com.example.todolist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.todolist.data.dao.TaskDao;
import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.enums.Priority;
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
                .allowMainThreadQueries() // w testach mozemy uzywac na main thread
                .build();
        taskDao = database.taskDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertAndGetTaskTest()  {
        Task task = new Task("Test task", LocalDateTime.now().plusDays(3), false, Priority.LOW, LocalDateTime.now());
        taskDao.insert(task);

        List<Task> allTasks = taskDao.getAllSync();
        assertEquals(1, allTasks.size());
        assertEquals("Test task", allTasks.getFirst().getTitle());
    }

    @Test
    public void deleteTaskTest() {
        Task task = new Task("Test task", LocalDateTime.now().plusDays(3), false, Priority.LOW, LocalDateTime.now());
        taskDao.insert(task);
        List<Task> allTasksBefore = taskDao.getAllSync();
        assertEquals(1, allTasksBefore.size());

        taskDao.delete(allTasksBefore.getFirst());
        List<Task> allTasksAfter = taskDao.getAllSync();
        assertTrue(allTasksAfter.isEmpty());
    }

    @Test
    public void updateTaskTest() {
        Task task = new Task("Original task", LocalDateTime.now().plusDays(3), false, Priority.LOW, LocalDateTime.now());
        taskDao.insert(task);
        List<Task> allTasks = taskDao.getAllSync();
        assertEquals(1, allTasks.size());

        Task insertedtask = allTasks.getFirst();
        insertedtask.setTitle("Updated task");
        insertedtask.setPriority(Priority.HIGH);
        taskDao.update(insertedtask);

        List<Task> updatedTasks = taskDao.getAllSync();
        assertEquals(1, updatedTasks.size());
        Task updatedTask = updatedTasks.getFirst();
        assertEquals("Updated task", updatedTask.getTitle());
        assertEquals(Priority.HIGH, updatedTask.getPriority());
    }

    @Test
    public void changeStatusTest() {
        Task task = new Task("Test task", LocalDateTime.now().plusDays(3), false, Priority.LOW, LocalDateTime.now());
        taskDao.insert(task);
        List<Task> allTasks = taskDao.getAllSync();
        assertEquals(1, allTasks.size());
        Task insertedTask = allTasks.getFirst();
        assertFalse(insertedTask.isDone());

        taskDao.changeStatus(insertedTask.getId(), !insertedTask.isDone());
        List<Task> changedTasks = taskDao.getAllSync();
        assertTrue(changedTasks.getFirst().isDone());
    }
}