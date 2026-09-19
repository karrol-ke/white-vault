package com.white_vault.app.data_base;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PasswordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Password password);

    @Query("SELECT * FROM passwords WHERE identifier = :identifier LIMIT 1")
    Password getPassword(String identifier);

    @Query("SELECT * FROM passwords")
    List<Password> getAllPasswords();

    @Query("DELETE FROM passwords WHERE identifier = :identifier")
    void deletePassword(String identifier);
}
