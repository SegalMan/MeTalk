package com.SegalTech.MeTalk;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp")
    LiveData<List<Message>> getAll();

    @Insert
    void insert(Message message);

    @Delete
    void delete(Message message);

    @Query("SELECT COUNT(*) FROM messages")
    int count();
}