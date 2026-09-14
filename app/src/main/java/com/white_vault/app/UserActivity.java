package com.white_vault.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserActivity extends AppCompatActivity {

    EditText text_user_name, text_user_password;
    Button button_create_database;
    String user_name, user_password, hashed_key;
    DataBaseManager dataBaseManager = new DataBaseManager();
    Cryptographic cryptographic = new Cryptographic();
    Intent intent;
    JsonHandler jsonHandler = new JsonHandler();

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

        button_create_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name = text_user_name.getText().toString();
                user_password = text_user_password.getText().toString();
                // Hashing Password
                hashed_key = cryptographic.hashPassword(user_password);
                dataBaseManager.createDatabase(UserActivity.this, hashed_key);
                // Saving user info
                jsonHandler.dumpValue(UserActivity.this, "user_info.json", "user_name", user_name);
                // Closing Database
                dataBaseManager.closeDataBase();
                intent = new Intent(UserActivity.this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}