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

    public boolean DataBaseExists(Context context){
        return DataBaseOperator.FileExists(context);
    }

    public void OpenDataBase(Context context, String key){
        try{
            db = DataBaseOperator.loadDataBase(context, key);
        } catch (Exception e) {
            Toast.makeText(context, "Error on Opening Database", Toast.LENGTH_SHORT).show();
        }
    }

    public interface ValueCallback {
        void onResult(String value);
    }
    public void FetchValue(String index, ValueCallback callback){
        databaseExecutor.execute(() -> {
            String result = db.passwordDao().getValue(index);
            callback.onResult(result);
        });
    }

}
