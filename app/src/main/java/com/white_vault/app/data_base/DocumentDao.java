package com.white_vault.app.data_base;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Document document);

    @Query("SELECT * FROM documents WHERE `identifier` = :identifier LIMIT 1")
    Document getValue(String identifier);

    @Query("SELECT * FROM documents")
    List<Document> getAllDocuments();
}
