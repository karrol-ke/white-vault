package com.white_vault.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AuthenticationActivity extends AppCompatActivity {

    EditText text_password;
    Button button_login;
    Cryptographic cryptographic = new Cryptographic();
    DataBaseManager dataBaseManager = new DataBaseManager();
    JsonHandler jsonHandler = new JsonHandler();
    Intent intent;
    String KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authentication);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        text_password = findViewById(R.id.text_password);
        button_login = findViewById(R.id.button_login);

        if (!jsonHandler.fileExists(this, "app_data.json")){
            Toast.makeText(this, "DataBase not found!, Let's create a new one.", Toast.LENGTH_LONG).show();
            intent = new Intent(AuthenticationActivity.this, UserActivity.class);
            startActivity(intent);
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KEY = cryptographic.hashPassword(text_password.getText().toString());
                if (dataBaseManager.authenticateDatabase(AuthenticationActivity.this, KEY)){
                    intent = new Intent (AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(AuthenticationActivity.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}