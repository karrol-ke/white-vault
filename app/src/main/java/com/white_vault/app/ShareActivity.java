package com.white_vault.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.SecureRandom;

public class ShareActivity extends AppCompatActivity {

    TextView text_one_time_key;
    Button button_stop_sharing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button_stop_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public static String generateOneTimeKey() {

        String characters = "abcdefghijklmnopqrstuvwxyz" + "0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder address = new StringBuilder(3);
        StringBuilder key = new StringBuilder(4);

        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(characters.length());
            address.append(characters.charAt(index));
        }

        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(characters.length());
            key.append(characters.charAt(index));
        }

        return address.toString() + "-" + key.toString();
    }
}