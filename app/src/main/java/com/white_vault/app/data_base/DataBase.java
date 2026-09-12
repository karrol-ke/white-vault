package com.white_vault.app.data_base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Password.class},
        version = 1,
        exportSchema = false
)
public abstract class DataBase extends RoomDatabase {
    public abstract PasswordDao passwordDao();
}
