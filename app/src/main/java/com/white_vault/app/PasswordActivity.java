package com.white_vault.app;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;

import com.google.android.material.card.MaterialCardView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PasswordActivity extends AppCompatActivity {
    LinearLayout container;
    ImageButton button_add;

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

        container = findViewById(R.id.password_view);
        button_add = findViewById(R.id.button_add);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(PasswordActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(40, 0, 40, 0);

                EditText new_title = new EditText(PasswordActivity.this);
                new_title.setHint("Title");

                EditText new_id = new EditText(PasswordActivity.this);
                new_id.setHint("Username / ID");

                EditText new_password = new EditText(PasswordActivity.this);
                new_password.setHint("Password");

                layout.addView(new_title);
                layout.addView(new_id);
                layout.addView(new_password);

                AlertDialog dialog = new AlertDialog.Builder(PasswordActivity.this)
                        .setTitle("Add New Password")
                        .setView(layout)
                        .setPositiveButton("Save", (dialogInterface, which) -> {

                            String date = "";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalDate currentDate = LocalDate.now();
                                date = currentDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
                            }

                            addCard(container, R.drawable.icon_key,
                                    new_title.getText().toString(),
                                    new_id.getText().toString(),
                                    date);

                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();
            }
        });
    }

    public void addCard(LinearLayout container, int iconResId, String title, String subtitle, String date) {

        Context context = this;

        MaterialCardView card = new MaterialCardView(context);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(context, 70));

        cardParams.setMargins(
                0,
                0,
                0,
                dpToPx(context, 8)
        );

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

        mainLayout.setPadding(
                dpToPx(context, 12),
                0,
                dpToPx(context, 12),
                0
        );


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


        TextView titleView = new TextView(context);

        titleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        titleView.setText(title);
        titleView.setTextColor(Color.parseColor("#F5F5F5"));

        titleView.setTextSize(15);
        titleView.setTypeface(titleView.getTypeface(), android.graphics.Typeface.BOLD);


        TextView subtitleView = new TextView(context);

        subtitleView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        subtitleView.setText(subtitle);
        subtitleView.setTextColor(Color.parseColor("#A1A1AA"));

        subtitleView.setTextSize(12);


        TextView dateView = new TextView(context);

        dateView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dateView.setText(date);
        dateView.setTextColor(Color.parseColor("#71717A"));

        dateView.setTextSize(11);

        textLayout.addView(titleView);
        textLayout.addView(subtitleView);

        mainLayout.addView(icon);
        mainLayout.addView(textLayout);
        mainLayout.addView(dateView);

        card.addView(mainLayout);
        container.addView(card);
    }


    // dp → px
    private int dpToPx(Context context, int dp) {

        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

}