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
import android.widget.Toast;

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

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasswordActivity extends AppCompatActivity {
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
                new_description.setHint("Description (Optional)");

                layout.addView(new_service_name);
                layout.addView(new_id);
                layout.addView(new_password);
                layout.addView(new_description);

                AlertDialog dialog = new AlertDialog.Builder(PasswordActivity.this)
                        .setTitle("Add New Password")
                        .setView(layout)
                        .setPositiveButton("Save", (dialogInterface, which) -> {

                            String identifier = generateIdentifier();
                            String id = new_id.getText().toString().trim();
                            String service = new_service_name.getText().toString();
                            String description = new_description.getText().toString();
                            String date;

                            if (id.isEmpty() || service.isEmpty() || new_password.getText().toString().isEmpty()){
                                Toast.makeText(PasswordActivity.this, "Empty values not acceptable", Toast.LENGTH_SHORT).show();
                            }else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")
                                    );
                                } else {
                                    date = "";
                                }

                                databaseExecutor.execute(() -> {db.passwordDao().insert(new Password(
                                        identifier, id, service, new_password.getText().toString(), date, description));
                                    runOnUiThread(() -> {
                                        loadPasswords();
                                    });
                                });
                            }

                        }).setNegativeButton("Cancel", null).create();

                dialog.show();
            }
        });
    }

    public void addCard(LinearLayout container, int iconResId, String identifier, String id, String service_name, String date) {

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


        TextView idView = new TextView(context);

        idView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        idView.setText(id);
        idView.setTextColor(Color.parseColor("#F5F5F5"));
        idView.setTextSize(15);
        idView.setTypeface(idView.getTypeface(), android.graphics.Typeface.BOLD);


        TextView serviceView = new TextView(context);

        serviceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        serviceView.setText(service_name);
        serviceView.setTextColor(Color.parseColor("#A1A1AA"));
        serviceView.setTextSize(12);


        TextView dateView = new TextView(context);

        dateView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dateView.setText(date);
        dateView.setTextColor(Color.parseColor("#71717A"));
        dateView.setTextSize(11);

        textLayout.addView(idView);
        textLayout.addView(serviceView);

        mainLayout.addView(icon);
        mainLayout.addView(textLayout);
        mainLayout.addView(dateView);

        // Listener for each card
        card.setOnClickListener(v -> {
            fetchPasswordDetails(identifier);
        });

        card.addView(mainLayout);
        container.addView(card);
    }


    // dp → px
    private int dpToPx(Context context, int dp) {

        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void showPasswordDetails(Password password) {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(this, 20), 0, dpToPx(this, 20), 0);

        TextView serviceView = new TextView(this);
        serviceView.setText("Service: " + password.service);

        TextView identifierView = new TextView(this);
        identifierView.setText("Username: " + password.id);

        TextView passwordView = new TextView(this);
        passwordView.setText("Password: " + password.value);
        passwordView.setTextIsSelectable(true);
        passwordView.setTextColor(Color.WHITE);

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
                .setNegativeButton("Delete", (dialog, which) -> {

                    new AlertDialog.Builder(this)
                            .setTitle("Delete Password")
                            .setMessage("Are you sure you want to delete this password?")
                            .setPositiveButton("Delete", (d, w) -> {
                                deletePassword(password.identifier);
                            })
                            .setNegativeButton("Cancel", null).show();
                }).show();
    }

    private void fetchPasswordDetails(String identifier) {

        databaseExecutor.execute(() -> {
            Password password = db.passwordDao().getPassword(identifier);
            runOnUiThread(() -> {
                if (password != null) {
                    showPasswordDetails(password);
                }else {
                    Toast.makeText(this, "Unable to fetch password details !", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadPasswords() {

        databaseExecutor.execute(() -> {
            List<Password> passwords = db.passwordDao().getAllPasswords();
            runOnUiThread(() -> {
                container.removeAllViews();

                for (Password password : passwords) {
                    addCard(
                            container,
                            R.drawable.icon_key,
                            password.identifier,
                            password.id,
                            password.service,
                            password.date
                    );
                }
            });
        });
    }

    private void deletePassword(String identifier) {
        databaseExecutor.execute(() -> {
            db.passwordDao().deletePassword(identifier);
            runOnUiThread(() -> {
                loadPasswords();
                Toast.makeText(PasswordActivity.this, "Password deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }

    public static String generateIdentifier() {

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