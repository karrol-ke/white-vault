package com.white_vault.app;

import android.content.Context;

import android.widget.Toast;

import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataBaseManager {
    public DataBase db;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    public void createDatabase(Context context, String key){
        boolean created = DataBaseOperator.createDatabase(context, key);
        if (created) {
            Toast.makeText(context, "Database created!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean authenticateDatabase(Context context, String key){
        boolean success = DataBaseOperator.authenticate(context, key);

        if (success) {
            db = DataBaseOperator.getDatabase();
            Toast.makeText(context, "Database opened!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    public void closeDataBase() {
        DataBaseOperator.closeDatabase();
    }

}
