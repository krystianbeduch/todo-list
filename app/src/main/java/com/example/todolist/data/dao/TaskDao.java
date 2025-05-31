package com.example.todolist.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.domain.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Task ORDER BY created_at DESC")
//    LiveData<List<Task>> getTasksOrderDescByCreatedDate();
    LiveData<List<Task>> getTasksOrderDescByCreatedDate();

//    @Query("SELECT * FROM Task ORDER BY LOWER(title)")
//    LiveData<List<Task>> getTasksOrderByTitle();
//
//    @Query("SELECT * FROM Task ORDER BY deadline")
//    LiveData<List<Task>> getTasksOrderByDeadline();
//
//    @Query("SELECT * FROM Task ORDER BY priority")
//    LiveData<List<Task>> getTasksOrderByPriority();
//
//    @Query("SELECT * FROM Task ORDER BY is_done")
//    LiveData<List<Task>> getTasksOrderByStatus();

    @Query("SELECT * FROM Task")
    List<Task>getAllSync();

    @Query("SELECT * FROM Task WHERE id = :id")
    LiveData<Task> getById(int id);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("DELETE FROM Task")
    void deleteAll();

    @Query("UPDATE Task SET is_done = :isDone WHERE id = :taskId")
    void changeStatus(int taskId, boolean isDone);
}
