package com.example.todolist.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.todolist.domain.model.Attachment;

import java.util.List;

@Dao
public interface AttachmentDao {
    @Query("SELECT * FROM Attachment")
    List<Attachment> getAllAttachments();
    @Query("SELECT * FROM Attachment WHERE task_id = :taskId")
    List<Attachment> getAttachmentsForTask(int taskId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Attachment attachment);

    @Delete
    void delete(Attachment attachment);
}
