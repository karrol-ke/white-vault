package com.white_vault.app;

import android.util.Base64;

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

    public String encrypt(String text, String key) {

        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

            return Base64.encodeToString(encrypted, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public String decrypt(String encryptedText, String key) {

        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] encrypted = Base64.decode(encryptedText, Base64.NO_WRAP);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}