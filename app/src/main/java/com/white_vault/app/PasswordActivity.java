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
import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;
import com.white_vault.app.data_base.Password;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordActivity extends AppCompatActivity {
    Cryptographic cryptographic = new Cryptographic();
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    LinearLayout container;
    ImageButton button_add;
    DataBase db = DataBaseOperator.getDatabase();


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

        loadPasswords();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(PasswordActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(40, 0, 40, 0);

                EditText new_service_name = new EditText(PasswordActivity.this);
                new_service_name.setHint("Service Name");

                EditText new_id = new EditText(PasswordActivity.this);
                new_id.setHint("Username / ID");

                EditText new_password = new EditText(PasswordActivity.this);
                new_password.setHint("Password");

                EditText new_description = new EditText(PasswordActivity.this);
                new_description.setHint("Description");

                layout.addView(new_service_name);
                layout.addView(new_id);
                layout.addView(new_password);
                layout.addView(new_description);

                AlertDialog dialog = new AlertDialog.Builder(PasswordActivity.this)
                        .setTitle("Add New Password")
                        .setView(layout)
                        .setPositiveButton("Save", (dialogInterface, which) -> {

                            String service = new_service_name.getText().toString();
                            String identifier = new_id.getText().toString().trim();
                            String description = new_description.getText().toString();
                            String date;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")
                                );
                            } else {
                                date = "";
                            }

                            databaseExecutor.execute(() -> {
                                db.passwordDao().insert(new Password(
                                                service,
                                                identifier,
                                                cryptographic.hashPassword(new_password.getText().toString()),
                                                date,
                                                description
                                        )
                                );
                                runOnUiThread(() -> {
                                    loadPasswords();
                                });
                            });
                        }).setNegativeButton("Cancel", null).create();

                dialog.show();
            }
        });
    }

    public void addCard(LinearLayout container, int iconResId, String service_name, String id, String date) {

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


        TextView serviceView = new TextView(context);

        serviceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        serviceView.setText(service_name);
        serviceView.setTextColor(Color.parseColor("#F5F5F5"));
        serviceView.setTextSize(15);
        serviceView.setTypeface(serviceView.getTypeface(), android.graphics.Typeface.BOLD);


        TextView idView = new TextView(context);

        idView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        idView.setText(id);
        idView.setTextColor(Color.parseColor("#A1A1AA"));
        idView.setTextSize(12);


        TextView dateView = new TextView(context);

        dateView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dateView.setText(date);
        dateView.setTextColor(Color.parseColor("#71717A"));
        dateView.setTextSize(11);

        textLayout.addView(serviceView);
        textLayout.addView(idView);

        mainLayout.addView(icon);
        mainLayout.addView(textLayout);
        mainLayout.addView(dateView);

        // Listener for each card
        card.setOnClickListener(v -> {
            fetchPasswordDetails(id);
        });

        card.addView(mainLayout);
        container.addView(card);
    }


    // dp → px
    private int dpToPx(Context context, int dp) {

        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void showPasswordDetails(Password password) {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                dpToPx(this, 20),
                0,
                dpToPx(this, 20),
                0
        );

        TextView serviceView = new TextView(this);
        serviceView.setText("Service: " + password.service);

        TextView identifierView = new TextView(this);
        identifierView.setText("Username: " + password.identifier);

        TextView passwordView = new TextView(this);
        passwordView.setText("Password: " + password.value);

        TextView dateView = new TextView(this);
        dateView.setText("Date: " + password.date);

        TextView descriptionView = new TextView(this);
        descriptionView.setText("Description: " + password.description);

        layout.addView(serviceView);
        layout.addView(identifierView);
        layout.addView(passwordView);
        layout.addView(dateView);
        layout.addView(descriptionView);

        new AlertDialog.Builder(this)
                .setTitle("Password Details")
                .setView(layout)
                .setPositiveButton("Close", null)
                .show();
    }

    private void fetchPasswordDetails(String service) {
        DataBase db = DataBaseOperator.getDatabase();
        databaseExecutor.execute(() -> {
            Password password = db.passwordDao().getPassword(service);
            runOnUiThread(() -> {
                if (password != null) {
                    showPasswordDetails(password);
                }
            });
        });
    }

    private void loadPasswords() {

        databaseExecutor.execute(() -> {

            List<Password> passwords =
                    db.passwordDao().getAllPasswords();

            runOnUiThread(() -> {
                container.removeAllViews();

                for (Password password : passwords) {
                    addCard(
                            container,
                            R.drawable.icon_key,
                            password.service,
                            password.identifier,
                            password.date
                    );
                }
            });
        });
    }

}