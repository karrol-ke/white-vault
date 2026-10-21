package com.white_vault.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;

import com.google.android.material.card.MaterialCardView;
import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;
import com.white_vault.app.data_base.Video;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoActivity extends AppCompatActivity {
    ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    DataBase db;
    Cryptographic cryptographic = new Cryptographic();
    LinearLayout container;
    String video_name;
    ImageButton button_add;
    private ActivityResultLauncher<String[]> filePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        container = findViewById(R.id.video_view);
        button_add = findViewById(R.id.button_add);

        db = DataBaseOperator.getDatabase();

        filePicker = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        encodeAndSaveFile(uri);
                    }
                }
        );

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddVideoDialog();
            }
        });

        loadVideos();
    }

    public void addCard(LinearLayout container, int iconResId, String identifier, String file_name, String date) {

        Context context = this;

        MaterialCardView card = new MaterialCardView(context);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(context, 70));

        cardParams.setMargins(0, 0, 0, dpToPx(context, 8));

        card.setLayoutParams(cardParams);
        card.setClickable(true);
        card.setFocusable(true);
        card.setCardBackgroundColor(Color.parseColor("#121212"));
        card.setRadius( dpToPx(context, 16));
        card.setCardElevation(0);
        card.setStrokeWidth( dpToPx(context, 1));
        card.setStrokeColor(Color.parseColor("#30FFFFFF"));

        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setGravity(Gravity.CENTER_VERTICAL);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);

        mainLayout.setPadding(dpToPx(context, 12), 0, dpToPx(context, 12), 0);

        ImageView icon = new ImageView(context);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dpToPx(context, 35), dpToPx(context, 35));
        icon.setLayoutParams(iconParams);
        icon.setImageResource(iconResId);

        ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(Color.parseColor("#7C3AED")));


        LinearLayout textLayout = new LinearLayout(context);

        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        textLayoutParams.setMarginStart(dpToPx(context, 12));
        textLayout.setLayoutParams(textLayoutParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);


        TextView fileName = new TextView(context);

        fileName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        fileName.setText(file_name);
        fileName.setTextColor(Color.parseColor("#F5F5F5"));
        fileName.setTextSize(15);
        fileName.setTypeface(fileName.getTypeface(), android.graphics.Typeface.BOLD);


        TextView dateView = new TextView(context);

        dateView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dateView.setText(date);
        dateView.setTextColor(Color.parseColor("#71717A"));
        dateView.setTextSize(11);

        textLayout.addView(fileName);

        mainLayout.addView(icon);
        mainLayout.addView(textLayout);
        mainLayout.addView(dateView);

        // Listener for each card
        card.setOnClickListener(v -> {
            showVideoDetails(identifier, file_name, date);
        });

        card.addView(mainLayout);
        container.addView(card);
    }

    // dp → px
    private int dpToPx(Context context, int dp) {

        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void showVideoDetails(String identifier, String file_name, String date) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(this, 20), 0, dpToPx(this, 20), 0);

        TextView imgNameView = new TextView(this);
        imgNameView.setText("Name : " + file_name);
        imgNameView.setTextColor(Color.WHITE);
        imgNameView.setTextSize(16);

        TextView dateView = new TextView(this);
        dateView.setText("Date: " + date);
        dateView.setTextColor(Color.WHITE);
        dateView.setTextSize(16);

        layout.addView(imgNameView);
        layout.addView(dateView);


        new AlertDialog.Builder(this).setTitle("Video Details").setView(layout)
                .setPositiveButton("View", (dialog, which) -> {
                    Intent intent = new Intent(VideoActivity.this, VideoViewActivity.class);
                    intent.putExtra("identifier", identifier);
                    startActivity(intent);
                }).setNegativeButton("Delete", (dialog, which) -> {

                    new AlertDialog.Builder(this).setTitle("Delete Video")
                            .setMessage("Are you sure you want to delete this Video ?")
                            .setPositiveButton("Delete", (d, w) -> {
                                deleteVideo(identifier);
                            }).setNegativeButton("Cancel", null).show();

                }).show();

    }

    private void showAddVideoDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(this, 40), 0, dpToPx(this, 40), 0);

        EditText img_name = new EditText(this);
        img_name.setHint("Video Name");

        layout.addView(img_name);

        new AlertDialog.Builder(this)
                .setTitle("Add New Video")
                .setView(layout)
                .setPositiveButton("Choose File", (dialog, which) -> {

                    video_name = img_name.getText().toString().trim();

                    if (video_name.isEmpty()) {
                        Toast.makeText(this, "Video name not be empty.", Toast.LENGTH_SHORT).show();
                    }else {
                        filePicker.launch(new String[]{"video/*"});
                    }
                }).setNegativeButton("Cancel", null).show();
    }

    private void encodeAndSaveFile(Uri uri) {

        databaseExecutor.execute(() -> {
            try {
                String encodedData = cryptographic.encodeFile(this, uri);
                String date = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
                }

                Video video = new Video(
                        generateIdentifier(),
                        video_name,
                        encodedData,
                        date
                );

                db.videoDao().insert(video);

                runOnUiThread(() -> {
                    loadVideos();
                    Toast.makeText(VideoActivity.this, "Video added", Toast.LENGTH_SHORT).show();
                });


            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(VideoActivity.this, "Failed to save video" , Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void loadVideos() {
        databaseExecutor.execute(() -> {
            List<Video> videos = db.videoDao().getAllVideo();
            runOnUiThread(() -> {
                container.removeAllViews();

                for (Video video : videos) {
                    addCard(
                            container,
                            R.drawable.icon_video,
                            video.identifier,
                            video.name,
                            video.date
                    );
                }
            });
        });
    }

    private void deleteVideo(String identifier){
        databaseExecutor.execute(() -> {
            db.videoDao().deleteVideo(identifier);
            runOnUiThread(() -> {
                loadVideos();
                Toast.makeText(this, "Image deleted.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private static String generateIdentifier() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789";

        int length = 7;

        SecureRandom random = new SecureRandom();
        StringBuilder identifier = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            identifier.append(characters.charAt(index));
        }

        DateTimeFormatter formatter = null;
        String currentDateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
            currentDateTime = LocalDateTime.now().format(formatter);
        }

        return identifier.toString() + "-" + currentDateTime;
    }
}