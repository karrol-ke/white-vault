package com.white_vault.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class PasswordsFragment extends Fragment {

    Context context;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    LinearLayout password_container;
    ImageButton button_add;
    DataBase db = DataBaseOperator.getDatabase();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment

        View view = inflater.inflate(R.layout.fragment_passwords, container, false);

        password_container = view.findViewById(R.id.password_container);
        button_add = view.findViewById(R.id.add_password);

        context = getContext();

        loadPasswords();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(requireContext());

                View dialogView = inflater.inflate(R.layout.dialog_add_password, null);

                EditText new_service_name = dialogView.findViewById(R.id.new_service_name);
                EditText new_id = dialogView.findViewById(R.id.new_id);
                EditText new_password = dialogView.findViewById(R.id.new_password);
                EditText new_description = dialogView.findViewById(R.id.new_description);

                TextView title = new TextView(requireContext());
                title.setText("Add New Password");
                title.setTextSize(20);
                title.setTextColor(Color.BLACK);
                title.setPadding(50, 40, 30, 20);

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setCustomTitle(title)
                        .setView(dialogView)
                        .setPositiveButton("Save", (dialogInterface, which) -> {

                            String identifier = generateIdentifier();
                            String id = new_id.getText().toString().trim();
                            String service = new_service_name.getText().toString();
                            String description = new_description.getText().toString();
                            String date;

                            if (id.isEmpty() || service.isEmpty() || new_password.getText().toString().isEmpty()){
                                Toast.makeText(context, "Empty values not acceptable", Toast.LENGTH_SHORT).show();
                            }else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")
                                    );
                                } else {
                                    date = "";
                                }

                                databaseExecutor.execute(() -> {db.passwordDao().insert(new Password(
                                        identifier, id, service, new_password.getText().toString(), date, description));
                                    requireActivity().runOnUiThread(() -> {
                                        loadPasswords();
                                    });
                                });
                            }

                        }).setNegativeButton("Cancel", null).create();

                dialog.show();

                dialog.getWindow().setBackgroundDrawableResource(R.drawable.home_page_bar);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#2d6a4f"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#b31237"));
            }
        });

        return view;
    }

    public void addCard(LinearLayout container, int iconResId, String identifier, String id, String service_name, String date) {

        View cardView = LayoutInflater.from(context).inflate(R.layout.item_password, container, false);
        MaterialCardView card = cardView.findViewById(R.id.password_card);

        ImageView icon = cardView.findViewById(R.id.password_icon);
        TextView idView = cardView.findViewById(R.id.password_id);
        TextView serviceView = cardView.findViewById(R.id.password_service);
        TextView dateView = cardView.findViewById(R.id.password_date);

        icon.setImageResource(iconResId);
        idView.setText(id);
        serviceView.setText(service_name);
        dateView.setText(date);

        card.setOnClickListener(v -> {
            fetchPasswordDetails(identifier);
        });

        container.addView(cardView);
    }


    // dp → px
    private int dpToPx(Context context, int dp) {

        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void showPasswordDetails(Password password) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(context, 20), 0, dpToPx(context, 20), 0);

        TextView serviceView = new TextView(context);
        serviceView.setText("Service: " + password.service);

        TextView identifierView = new TextView(context);
        identifierView.setText("Username: " + password.id);

        TextView passwordView = new TextView(context);
        passwordView.setText("Password: " + password.value);
        passwordView.setTextIsSelectable(true);
        passwordView.setTextColor(Color.WHITE);

        TextView dateView = new TextView(context);
        dateView.setText("Date: " + password.date);

        TextView descriptionView = new TextView(context);
        descriptionView.setText("Description: " + password.description);

        layout.addView(serviceView);
        layout.addView(identifierView);
        layout.addView(passwordView);
        layout.addView(dateView);
        layout.addView(descriptionView);

        new AlertDialog.Builder(context)
                .setTitle("Password Details")
                .setView(layout)
                .setPositiveButton("Close", null)
                .setNegativeButton("Delete", (dialog, which) -> {

                    new AlertDialog.Builder(context)
                            .setTitle("Delete Password")
                            .setMessage("Are you sure you want to delete context password?")
                            .setPositiveButton("Delete", (d, w) -> {
                                deletePassword(password.identifier);
                            })
                            .setNegativeButton("Cancel", null).show();
                }).show();
    }

    private void fetchPasswordDetails(String identifier) {

        databaseExecutor.execute(() -> {
            Password password = db.passwordDao().getPassword(identifier);
            requireActivity().runOnUiThread(() -> {
                if (password != null) {
                    showPasswordDetails(password);
                }else {
                    Toast.makeText(context, "Unable to fetch password details !", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadPasswords() {

        databaseExecutor.execute(() -> {
            List<Password> passwords = db.passwordDao().getAllPasswords();
            requireActivity().runOnUiThread(() -> {
                password_container.removeAllViews();

                for (Password password : passwords) {
                    addCard(
                            password_container,
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
            requireActivity().runOnUiThread(() -> {
                loadPasswords();
                Toast.makeText(context, "Password deleted", Toast.LENGTH_SHORT).show();
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