package com.white_vault.app.data_base;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Document document);

    @Query("SELECT * FROM document WHERE `identifier` = :identifier LIMIT 1")
    Document getValue(String identifier);

    @Query("SELECT data_one FROM document WHERE `identifier` = :identifier LIMIT 1")
    String getDataOne(String identifier);

    @Query("SELECT data_two FROM document WHERE `identifier` = :identifier LIMIT 1")
    String getDataTwo(String identifier);
}
