package com.white_vault.app;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Cryptographic {
    /**
     * Hashes an input string using SHA-256 and returns a hex string.
     *
     * @param password The string to hash
     * @return The 64-character hexadecimal SHA-256 hash
     */

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded_hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encoded_hash.length);
            for (byte b : encoded_hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public String encodeFile(Context context, Uri uri) throws Exception {

        try (
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            if (inputStream == null) {
                throw new Exception("Unable to open selected file");
            }
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        }
    }

    public File decodeFile(Context context, String base64Pdf) throws Exception {

        byte[] pdfBytes = Base64.decode(base64Pdf, Base64.DEFAULT);
        File pdfFile = File.createTempFile("vault_", ".pdf", context.getCacheDir());

        try (FileOutputStream output = new FileOutputStream(pdfFile)) {
            output.write(pdfBytes);
        }
        return pdfFile;
    }

}