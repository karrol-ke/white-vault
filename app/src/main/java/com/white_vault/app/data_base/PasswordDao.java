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

    @Query("SELECT * FROM passwords WHERE service = :service LIMIT 1")
    Password getPassword(String service);

    @Query("SELECT * FROM passwords")
    List<Password> getAllPasswords();
}
