package com.white_vault.app;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;
import com.white_vault.app.data_base.Image;
import com.white_vault.app.data_base.Video;
import com.white_vault.app.data_base.VideoDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoViewActivity extends AppCompatActivity {
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private DataBase db;
    Cryptographic cryptographic = new Cryptographic();
    VideoView video_view;
    String identifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        video_view = findViewById(R.id.videoView);

        db = DataBaseOperator.getDatabase();
        identifier = getIntent().getStringExtra("identifier");

        loadVideo(identifier);

    }

    private void loadVideo(String identifier){
        databaseExecutor.execute(() -> {
            try {
                Video video = db.videoDao().getVideo(identifier);
                runOnUiThread(() -> {
                    try {
                        video_view.setVideoPath(cryptographic.decodeVideo(this, video.data).getAbsolutePath());
                        video_view.setMediaController(new MediaController(this));
                        video_view.start();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}