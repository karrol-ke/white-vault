package com.white_vault.app;

import android.content.Context;

import android.widget.Toast;

import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;
import com.white_vault.app.data_base.Document;
import com.white_vault.app.data_base.Password;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataBaseManager {
    public DataBase db;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    public DataBase getDatabase() {
        return db;
    }

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

    public interface PasswordCallback {
        void onResult(String value);
    }

    public interface DocumentCallback {
        void onResult(String data_one, String data_two);
    }
    public void fetchPassword(String identifier, PasswordCallback callback){
        databaseExecutor.execute(() -> {
            Password result = db.passwordDao().getPassword(identifier);
        });
    }

    public void fetchDocument(String identifier, DocumentCallback callback){
        databaseExecutor.execute(() -> {
            Document document = db.documentDao().getValue(identifier);
            String data_one = document.data_one;
            String data_two = document.data_two;
            callback.onResult(data_one, data_two);
        });
    }
    public void insertPassword(String identifier, String service_name, String value, String date, String description){
        databaseExecutor.execute(() -> {
            Password password = new Password(identifier, service_name, value, date, description);
            db.passwordDao().insert(password);
        });
    }

    public void insertDocument(String identifier, String data_one, String data_two){
        databaseExecutor.execute(() -> {
            Document document = new Document(identifier, data_one, data_two);
            db.documentDao().insert(document);
        });
    }

    public void closeDataBase() {
        DataBaseOperator.closeDatabase();
    }

}
