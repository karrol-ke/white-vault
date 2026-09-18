package com.white_vault.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements DefaultLifecycleObserver {
    DataBaseManager dataBaseManager = new DataBaseManager();
    MaterialCardView card_password, card_document;
    TextView text_user_name;
    Intent intent;
    String user_name;

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

        user_name = JsonHandler.getValue(this, "app_data.json", "user_info", "user_name");

        text_user_name = findViewById(R.id.text_user_name);
        card_password = findViewById(R.id.card_password);
        card_document = findViewById(R.id.card_document);

        text_user_name.setText(user_name);

        card_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStop(@NotNull LifecycleOwner owner){
        dataBaseManager.closeDataBase();
    }
}