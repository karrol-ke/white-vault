package com.white_vault.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Base64InputStream;
import android.widget.VideoView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;


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

    public Bitmap decodeImage(String encode_data) {

        byte[] imageBytes = Base64.decode(
                encode_data,
                Base64.DEFAULT
        );

        Bitmap bitmap = BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.length
        );

        return bitmap;
    }

    public File decodeVideo(Context context, String encode_data) throws IOException {

        File videoFile = File.createTempFile("vault_", ".mp4", context.getFilesDir());

        try (
                InputStream input = new Base64InputStream(
                        new ByteArrayInputStream(
                                encode_data.getBytes(StandardCharsets.US_ASCII)
                        ),
                        Base64.DEFAULT
                );
                OutputStream output = new FileOutputStream(videoFile)
        ) {
            byte[] buffer = new byte[8192];
            int length;

            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
        } catch (IOException e) {
            videoFile.delete();
            throw e;
        }

        return videoFile;
    }

    public File decodePDF(Context context, String encode_data) throws Exception {

        byte[] pdfBytes = Base64.decode(encode_data, Base64.DEFAULT);
        File pdfFile = File.createTempFile("vault_", ".pdf", context.getCacheDir());

        try (FileOutputStream output = new FileOutputStream(pdfFile)) {
            output.write(pdfBytes);
        }
        return pdfFile;
    }

    public String encrypt(String plainText, String key) throws Exception {

        // Convert 64 hex characters → 32 bytes
        byte[] keyBytes = new byte[32];

        for (int i = 0; i < 32; i++) {
            keyBytes[i] = (byte) Integer.parseInt(key.substring(i * 2, i * 2 + 2), 16);
        }

        SecretKeySpec key_spec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key_spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }

}