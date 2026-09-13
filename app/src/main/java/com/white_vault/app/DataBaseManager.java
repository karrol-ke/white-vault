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

    public void openDatabase(Context context, String key){
        try{
            db = DataBaseOperator.loadDataBase(context, key);
        } catch (Exception e) {
            Toast.makeText(context, "Error on Opening Database", Toast.LENGTH_SHORT).show();
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
            String result = db.passwordDao().getValue(identifier);
            callback.onResult(result);
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
    public void insertPassword(String identifier, String value){
        databaseExecutor.execute(() -> {
            Password password = new Password(identifier, value);
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
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

}
