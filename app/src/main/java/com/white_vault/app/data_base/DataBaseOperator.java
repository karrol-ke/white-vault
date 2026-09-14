package com.white_vault.app.data_base;

import android.content.Context;

import androidx.room.Room;

import net.zetetic.database.sqlcipher.SupportOpenHelperFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class DataBaseOperator {
    private static DataBase dataBase;
    private static final String DATABASE_NAME = "white_vault.db";

    public static boolean databaseExists(Context context) {

        File databaseFile = context.getDatabasePath(DATABASE_NAME);

        return databaseFile.exists();
    }

    // Create a new database
    public static boolean createDatabase(Context context, String DATABASE_KEY) {

        if (databaseExists(context)) {
            return false;
        }

        try {

            System.loadLibrary("sqlcipher");
            byte[] passphrase = DATABASE_KEY.getBytes(StandardCharsets.UTF_8);
            SupportOpenHelperFactory factory = new SupportOpenHelperFactory(passphrase);
            DataBase newDatabase = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DataBase.class,
                            DATABASE_NAME
                    )
                    .openHelperFactory(factory)
                    .build();

            // Force database creation/opening
            newDatabase.getOpenHelper().getWritableDatabase();
            dataBase = newDatabase;
            return true;

        } catch (Exception e) {

            e.printStackTrace();

            dataBase = null;

            return false;
        }
    }

    // Authenticate existing database
    public static boolean authenticate(Context context, String DATABASE_KEY) {

        if (!databaseExists(context)) {
            return false;
        }
        // Close an old connection first
        closeDatabase();
        DataBase testDatabase = null;

        try {

            System.loadLibrary("sqlcipher");
            byte[] passphrase = DATABASE_KEY.getBytes(StandardCharsets.UTF_8);
            SupportOpenHelperFactory factory = new SupportOpenHelperFactory(passphrase);
            testDatabase = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DataBase.class,
                            DATABASE_NAME
                    )
                    .openHelperFactory(factory)
                    .build();

            // This actually forces SQLCipher to open/authenticate
            testDatabase.getOpenHelper().getWritableDatabase();
            dataBase = testDatabase;

            return true;

        } catch (Exception e) {
            if (testDatabase != null) {
                testDatabase.close();
            }

            dataBase = null;

            return false;
        }
    }

    // Get currently opened database
    public static DataBase getDatabase() {
        return dataBase;
    }

    // Close database
    public static void closeDatabase() {

        if (dataBase != null) {
            if (dataBase.isOpen()) {
                dataBase.close();
            }
            dataBase = null;
        }
    }

}