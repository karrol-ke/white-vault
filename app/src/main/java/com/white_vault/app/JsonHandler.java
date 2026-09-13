package com.white_vault.app;

import android.content.Context;
import android.os.Build;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonHandler {
    public String getValue(Context context, String file_name, String key) {
        try {
            InputStream inputStream = context.getAssets().open(file_name);

            byte[] data = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data = inputStream.readAllBytes();
            }
            inputStream.close();

            String jsonString = new String(data, StandardCharsets.UTF_8);

            return new JSONObject(jsonString).getString(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void dumpValue(Context context, String file_name, String key, String value) {

        File file = new File(context.getFilesDir(), file_name);

        JSONObject json;

        // File exists → read existing JSON
        try {
            if (file.exists() && file.length() > 0) {

                try (FileInputStream input = new FileInputStream(file)) {

                    byte[] data = new byte[(int) file.length()];
                    int length = input.read(data);

                    String jsonString =
                            new String(data, 0, length, StandardCharsets.UTF_8);

                    json = new JSONObject(jsonString);
                }

            } else {
                // File doesn't exist → create new JSON object
                json = new JSONObject();
            }

            // Add or modify data
            JSONObject account = new JSONObject();
            account.put(key, value);


            // Write JSON back to the file
            try (FileOutputStream output = new FileOutputStream(file)) {

                output.write(json.toString(4).getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean fileExists(Context context, String file_name) {

        File file = new File(context.getFilesDir(), file_name);

        return file.exists();
    }

}