package com.white_vault.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    Intent intent;
    JsonHandler jsonHandler = new JsonHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            startUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void startUp() throws Exception {
        if (jsonHandler.fileExists(this, "user_info.json")){

            intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            startActivity(intent);

        }else {
            Toast.makeText(this, "DataBase not found!, Let's create a new one.", Toast.LENGTH_LONG).show();
            intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        }
    }
}