package com.white_vault.app;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void createCard() {
        LinearLayout container = findViewById(R.id.password_view);

        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(100));

        cardParams.setMargins(
                dpToPx(8),
                dpToPx(8),
                dpToPx(8),
                dpToPx(8)
        );

        card.setLayoutParams(cardParams);

        card.setRadius(dpToPx(20));
        card.setCardElevation(dpToPx(4));

        card.setCardBackgroundColor(Color.parseColor(String.valueOf(R.color.card)));

        card.setStrokeWidth(dpToPx(1));
        card.setStrokeColor(Color.parseColor(String.valueOf(R.color.stoke)));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);

        content.setPadding(
                dpToPx(16),
                dpToPx(16),
                dpToPx(16),
                dpToPx(16)
        );

        TextView title = new TextView(this);

        title.setText("Gmail");
        title.setTextColor(Color.WHITE);
        title.setTextSize(18);

        TextView subtitle = new TextView(this);
        subtitle.setText("myemail@gmail.com");
        subtitle.setTextColor(Color.parseColor("#A1A1AA"));
        subtitle.setTextSize(13);

        content.addView(title);
        content.addView(subtitle);
        card.addView(content);
        container.addView(card);
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }
}