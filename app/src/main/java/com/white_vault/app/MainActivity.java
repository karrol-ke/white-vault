package com.white_vault.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements DefaultLifecycleObserver {
    DataBaseManager dataBaseManager = new DataBaseManager();
    FirebaseDatabase realtime_database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    BottomNavigationView navigation_bar;
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

        navigation_bar = findViewById(R.id.navigation_bar);

        FragmentView(new PasswordsFragment());
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

        navigation_bar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_passwords){
                FragmentView(new PasswordsFragment());
            }else if (item.getItemId() == R.id.navigation_documents){
                FragmentView(new DocumentsFragment());
            }else if (item.getItemId() == R.id.navigation_images){
                FragmentView(new ImagesFragment());
            }else if (item.getItemId() == R.id.navigation_settings){
                FragmentView(new SettingsFragment());
            }

            return true;
        });
    }

    private void FragmentView(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_view, fragment);
        fragmentTransaction.commit();
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