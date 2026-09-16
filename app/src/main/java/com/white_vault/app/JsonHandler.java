package com.white_vault.app;

import android.content.Context;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class JsonHandler {

    public String getValue(Context context, String fileName, String parentKey, String key){
        try {
            File file = new File(context.getFilesDir(), fileName);

            if (!file.exists()) {
                return null;
            }

            try (FileInputStream input = new FileInputStream(file)) {

                byte[] data = new byte[(int) file.length()];
                int length = input.read(data);

                String jsonString = new String(
                        data,
                        0,
                        length,
                        StandardCharsets.UTF_8
                );

                JSONObject root = new JSONObject(jsonString);

                if (!root.has(parentKey)) {
                    return null;
                }

                JSONObject parent = root.getJSONObject(parentKey);

                return parent.optString(key, null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void dumpValue(Context context, String fileName, String parentKey, String key, String value) {
        File file = new File(context.getFilesDir(), fileName);

        try {
            JSONObject root;
            if (file.exists() && file.length() > 0) {

                try (FileInputStream input = new FileInputStream(file)) {
                    byte[] data = new byte[(int) file.length()];
                    int length = input.read(data);

                    String jsonString = new String(
                            data,
                            0,
                            length,
                            StandardCharsets.UTF_8
                    );

                    root = new JSONObject(jsonString);
                }

            } else {
                root = new JSONObject();
            }
            JSONObject parent;

            if (root.has(parentKey)) {
                parent = root.getJSONObject(parentKey);
            } else {
                parent = new JSONObject();
                root.put(parentKey, parent);
            }

            // Add or modify value
            parent.put(key, value);

            try (FileOutputStream output = new FileOutputStream(file)) {
                output.write(root.toString(4).getBytes(StandardCharsets.UTF_8));
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