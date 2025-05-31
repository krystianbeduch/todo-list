package com.example.todolist.domain.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.todolist.data.dao.AttachmentDao;
import com.example.todolist.data.dao.TaskDao;
import com.example.todolist.data.db.AppDatabase;
import com.example.todolist.domain.model.Attachment;
import com.example.todolist.domain.model.Task;

import java.util.List;

public class AttachmentRepository {
    private final AttachmentDao attachmentDao;

    public AttachmentRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        attachmentDao = db.attachmentDao();
    }

    public List<Attachment> getAttachmentsByTaskId(int id) {
        return attachmentDao.getAttachmentsForTask(id);
    }

    public void insertAttachment(Attachment attachment) {
        attachmentDao.insert(attachment);
    }

    public void deleteAttachment(Attachment attachment) {
        attachmentDao.delete(attachment);
    }

}
