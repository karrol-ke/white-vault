package com.white_vault.app;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;
import com.white_vault.app.data_base.Image;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageViewActivity extends AppCompatActivity {
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private DataBase db;
    Cryptographic cryptographic = new Cryptographic();
    ImageView image_view;
    String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        image_view = findViewById(R.id.imageView);

        db = DataBaseOperator.getDatabase();
        identifier = getIntent().getStringExtra("identifier");

        loadImage(identifier);
    }

    private void loadImage(String identifier){
        databaseExecutor.execute(() -> {
            try {
                Image image = db.imageDao().getImage(identifier);
                runOnUiThread(() -> {
                    image_view.setImageBitmap(cryptographic.decodeImage(image.data));
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}