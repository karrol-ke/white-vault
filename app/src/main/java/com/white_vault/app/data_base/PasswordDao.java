package com.white_vault.app.data_base;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Password password);

    @Query("SELECT value FROM passwords WHERE `index` = :index LIMIT 1")
    String getValue(String index);
}
