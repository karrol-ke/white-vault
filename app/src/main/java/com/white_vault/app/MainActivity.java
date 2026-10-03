package com.white_vault.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements DefaultLifecycleObserver {
    DataBaseManager dataBaseManager = new DataBaseManager();
    FirebaseDatabase realtime_database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    MaterialCardView card_password, card_document;
    TextView text_user_name;
    Intent intent;
    String user_name;
    String releases_url = "https://github.com/karrol-ke/white-vault/releases";

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

        reference = realtime_database.getReference("update");

        try {
            String current_app_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            reference.child("version").get().addOnCompleteListener(task -> {
                DataSnapshot version_snapshot = task.getResult();
                String last_version = version_snapshot.getValue(String.class);

                if (!Objects.equals(current_app_version, last_version)){
                    reference.child("info").get().addOnCompleteListener(task1 -> {
                        DataSnapshot info_snapshot = task1.getResult();
                        String version_info = info_snapshot.getValue(String.class);
                        showUpdateAvailable(last_version, version_info);
                    });
                }
            });
        } catch (Exception e) {
            //
        }

        card_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, PasswordActivity.class);
                startActivity(intent);
            }
        });

        card_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, DocumentActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showUpdateAvailable(String v, String info) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(this, 20), 0, dpToPx(this, 20), 0);

        TextView versionName = new TextView(this);
        versionName.setText("White Vault v" +v);
        versionName.setTextColor(Color.WHITE);
        versionName.setTextSize(25);

        TextView infoView = new TextView(this);
        infoView.setText(info);
        infoView.setTextSize(16);

        layout.addView(versionName);
        layout.addView(infoView);

        new AlertDialog.Builder(this).setTitle("Update Available").setView(layout)
                .setPositiveButton("Update", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(releases_url));
                    startActivity(intent);

                }).setNegativeButton("Close", null).show();

    }

    private int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public void onStop(@NotNull LifecycleOwner owner){
        dataBaseManager.closeDataBase();
    }
}