package com.white_vault.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;
import com.white_vault.app.data_base.Document;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShareActivity extends AppCompatActivity {

    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    Cryptographic cryptographic = new Cryptographic();
    private DataBase db;
    TextView text_one_time_key;
    Button button_stop_sharing;
    private String PDF_data;

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

        db = DataBaseOperator.getDatabase();

        String identifier = getIntent().getStringExtra("id");
        String[] one_time_key = generateOneTimeKey().split("-");
        startSharing(identifier, one_time_key[1], one_time_key[0]);

        button_stop_sharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private static String generateOneTimeKey() {

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

    private void startSharing(String identifier, String key, String address){
        databaseExecutor.execute(() -> {
            try {
                Document document = db.documentDao().getValue(identifier);

                if (document == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }else {
                    PDF_data = cryptographic.encrypt(document.data, key);
                }

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to open PDF", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}