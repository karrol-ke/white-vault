package com.white_vault.app.data_base;

import android.content.Context;
import android.widget.Toast;

import androidx.room.Room;

import net.zetetic.database.sqlcipher.SupportOpenHelperFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class DataBaseOperator {
    private static DataBase dataBase;

    public static DataBase loadDataBase(Context context, String DATABASE_KEY){

        if (dataBase == null){
            System.loadLibrary("sqlcipher");
            byte[] passphrase = DATABASE_KEY.getBytes(StandardCharsets.UTF_8);
            SupportOpenHelperFactory factory = new SupportOpenHelperFactory(passphrase);

            dataBase = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DataBase.class,
                            "white_vault.db"
            ).openHelperFactory(factory).build();

        }
        Toast.makeText(context, "Database Created!", Toast.LENGTH_SHORT).show();
        return dataBase;
    }
}
