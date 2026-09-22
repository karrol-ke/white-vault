package com.white_vault.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserActivity extends AppCompatActivity {

    EditText text_user_name, text_user_password;
    Button button_create_database;
    ProgressBar progress_bar;
    String user_name, user_password, hashed_key;
    DataBaseManager dataBaseManager = new DataBaseManager();
    Cryptographic cryptographic = new Cryptographic();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        text_user_name = findViewById(R.id.user_name);
        text_user_password = findViewById(R.id.user_password);
        button_create_database = findViewById(R.id.button_create_database);
        progress_bar = findViewById(R.id.progress_bar);

        button_create_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_create_database.setVisibility(View.INVISIBLE);
                progress_bar.setVisibility(View.VISIBLE);

                progress_bar.post(() -> {
                    user_name = text_user_name.getText().toString();
                    user_password = text_user_password.getText().toString();

                    if (user_name.isEmpty() || user_password.isEmpty()){
                        Toast.makeText(UserActivity.this, "Please provide your credentials", Toast.LENGTH_LONG).show();
                        progress_bar.setVisibility(View.INVISIBLE);
                        button_create_database.setVisibility(View.VISIBLE);
                    }else {
                        // Hashing Password
                        hashed_key = cryptographic.hashPassword(user_password);
                        dataBaseManager.createDatabase(UserActivity.this, hashed_key);
                        // Saving user info
                        JsonHandler.dumpValue(UserActivity.this, "app_data.json", "user_info", "user_name", user_name);
                        // Closing Database
                        dataBaseManager.closeDataBase();
                        intent = new Intent(UserActivity.this, AuthenticationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });


    }
}