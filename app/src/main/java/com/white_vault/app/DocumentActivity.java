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
import android.widget.ProgressBar;
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
import com.white_vault.app.data_base.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DocumentActivity extends AppCompatActivity {

    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    Cryptographic cryptographic = new Cryptographic();
    ImageButton button_add;
    ProgressBar progress_bar;
    private LinearLayout container;
    private DataBase db;
    private String pendingIdentifier;
    private String pendingDescription;
    private ActivityResultLauncher<String[]> filePicker;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_document);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        container = findViewById(R.id.document_view);
        button_add = findViewById(R.id.button_add);
        progress_bar = findViewById(R.id.progress_bar);

        db = DataBaseOperator.getDatabase();

        if (db == null) {
            Toast.makeText(this, "Database is null", Toast.LENGTH_SHORT).show();
            return;
        }

        // File picker
        filePicker = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        encodeAndSaveFile(uri);
                    }
                }
        );

        loadDocuments();
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDocumentDialog();
            }
        });
    }

    private void showAddDocumentDialog() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(this, 40), 0, dpToPx(this, 40), 0);

        EditText newIdentifier = new EditText(this);
        newIdentifier.setHint("Document Name");

        EditText newDescription = new EditText(this);
        newDescription.setHint("Description");

        layout.addView(newIdentifier);
        layout.addView(newDescription);

        new AlertDialog.Builder(this)
                .setTitle("Add New Document")
                .setView(layout)
                .setPositiveButton("Choose File", (dialog, which) -> {

                    pendingIdentifier = newIdentifier.getText().toString().trim();
                    pendingDescription = newDescription.getText().toString().trim();

                    if (pendingIdentifier.isEmpty()) {
                        return;
                    }

                    filePicker.launch(new String[]{"*/*"});
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

                Document document = new Document(
                                pendingIdentifier,
                                encodedData,
                                date,
                                pendingDescription
                );

                db.documentDao().insert(document);

                runOnUiThread(() -> {
                    loadDocuments();
                    Toast.makeText(DocumentActivity.this, "Document added", Toast.LENGTH_SHORT).show();
                });


            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(DocumentActivity.this, "Failed to save document" , Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public void addCard(LinearLayout container, int iconResId, String identifier, String date, String description) {

        Context context = this;

        MaterialCardView card = new MaterialCardView(context);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(context, 70));
        cardParams.setMargins(0, 0, 0, dpToPx(context, 8));
        card.setLayoutParams(cardParams);
        card.setClickable(true);
        card.setFocusable(true);
        card.setCardBackgroundColor(Color.parseColor("#121212"));
        card.setRadius(dpToPx(context, 16));
        card.setCardElevation(0);
        card.setStrokeWidth(dpToPx(context, 1));
        card.setStrokeColor(Color.parseColor("#30FFFFFF"));


        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );
        mainLayout.setGravity(Gravity.CENTER_VERTICAL);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setPadding(dpToPx(context, 12), 0, dpToPx(context, 12), 0);


        ImageView icon = new ImageView(context);
        icon.setLayoutParams(new LinearLayout.LayoutParams(
                        dpToPx(context, 35),
                        dpToPx(context, 35)
                )
        );
        icon.setImageResource(iconResId);

        ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(Color.parseColor("#7C3AED")));


        LinearLayout textLayout = new LinearLayout(context);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textParams.setMarginStart(dpToPx(context, 12));
        textLayout.setLayoutParams(textParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);


        TextView identifierView = new TextView(context);
        identifierView.setText(identifier);
        identifierView.setTextColor(Color.parseColor("#F5F5F5"));
        identifierView.setTextSize(15);
        identifierView.setTypeface(identifierView.getTypeface(), android.graphics.Typeface.BOLD);


        TextView descriptionView = new TextView(context);
        descriptionView.setText(description);
        descriptionView.setTextColor(Color.parseColor("#A1A1AA"));
        descriptionView.setTextSize(12);


        TextView dateView = new TextView(context);
        dateView.setText(date);
        dateView.setTextColor(Color.parseColor("#71717A"));
        dateView.setTextSize(11);


        textLayout.addView(identifierView);
        textLayout.addView(descriptionView);

        mainLayout.addView(icon);
        mainLayout.addView(textLayout);
        mainLayout.addView(dateView);

        card.addView(mainLayout);


        card.setOnClickListener(v -> {

            fetchDocumentDetails(identifier);

        });

        container.addView(card);
    }

    private void fetchDocumentDetails(String identifier) {

        databaseExecutor.execute(() -> {
            Document document = db.documentDao().getValue(identifier);
            runOnUiThread(() -> {
                if (document != null) {
                    showDocumentDetails(document);
                }
            });
        });
    }

    private void showDocumentDetails(Document document) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dpToPx(this, 20), 0, dpToPx(this, 20), 0);

        TextView identifierView = new TextView(this);
        identifierView.setText("Name : " + document.identifier);
        identifierView.setTextColor(Color.WHITE);
        identifierView.setTextSize(16);

        TextView dateView = new TextView(this);
        dateView.setText("Date: " + document.date);
        dateView.setTextColor(Color.WHITE);
        dateView.setTextSize(16);

        TextView descriptionView = new TextView(this);
        descriptionView.setText("Description: " + document.description);
        descriptionView.setTextColor(Color.WHITE);
        descriptionView.setTextSize(16);

        TextView dataView = new TextView(this);
        dataView.setText("Decode size: " + document.data.length() + " characters");
        dataView.setTextColor(Color.parseColor("#A1A1AA"));
        dataView.setTextSize(13);

        layout.addView(identifierView);
        layout.addView(dateView);
        layout.addView(descriptionView);
        layout.addView(dataView);

        new AlertDialog.Builder(this).setTitle("Document Details").setView(layout)
                .setPositiveButton("View", (dialog, which) -> {
                    intent = new Intent(DocumentActivity.this, DocumentViewActivity.class);
                    intent.putExtra("id", document.identifier);
                    startActivity(intent);
                }).setNegativeButton("Delete", (dialog, which) -> {

                    new AlertDialog.Builder(this).setTitle("Delete Document")
                            .setMessage("Are you sure you want to delete this document ?")
                            .setPositiveButton("Delete", (d, w) -> {
                                deleteDocument(document.identifier);
                            }).setNegativeButton("Cancel", null).show();

                }).show();

    }

    private void loadDocuments() {
        if (db == null) {
            progress_bar.setVisibility(View.INVISIBLE);
            return;
        }

        databaseExecutor.execute(() -> {
            List<Document> documents = db.documentDao().getAllDocuments();
            runOnUiThread(() -> {
                container.removeAllViews();
                for (Document document : documents) {
                    addCard(
                            container,
                            R.drawable.icon_document,
                            document.identifier,
                            document.date,
                            document.description
                    );
                }
                progress_bar.setVisibility(View.INVISIBLE);
            });
        });
    }

    //dp -> px
    private int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private void deleteDocument(String identifier) {
        databaseExecutor.execute(() -> {
            db.documentDao().deleteDocument(identifier);
            runOnUiThread(() -> {
                loadDocuments();
                Toast.makeText(DocumentActivity.this, "Document deleted", Toast.LENGTH_SHORT).show();
            });
        });
    }
}