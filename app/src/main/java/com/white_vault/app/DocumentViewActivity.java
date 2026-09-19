package com.white_vault.app;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.white_vault.app.data_base.DataBase;
import com.white_vault.app.data_base.DataBaseOperator;
import com.white_vault.app.data_base.Document;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DocumentViewActivity extends AppCompatActivity {
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    Cryptographic cryptographic = new Cryptographic();
    ImageView pdfPage;
    TextView text_page_number;
    Button button_previous;
    Button button_next;
    private DataBase db;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private File temporaryPdf;
    private int currentPageIndex = 0;
    private int pageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_document_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pdfPage = findViewById(R.id.pdfPage);
        text_page_number = findViewById(R.id.text_page_number);
        button_previous = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        String identifier = getIntent().getStringExtra("id");

        db = DataBaseOperator.getDatabase();

        if (db == null || identifier == null) {
            Toast.makeText(this, "Unable to open document", Toast.LENGTH_SHORT).show();
            finish();

            return;
        }

        button_previous.setOnClickListener(v -> {
            if (currentPageIndex > 0) {
                currentPageIndex--;
                renderPage(currentPageIndex);
            }
        });

        button_next.setOnClickListener(v -> {
            if (currentPageIndex < pageCount - 1) {
                currentPageIndex++;
                renderPage(currentPageIndex);
            }
        });

        loadDocument(identifier);
    }

    private void loadDocument(String identifier) {

        databaseExecutor.execute(() -> {
            try {
                Document document = db.documentDao().getValue(identifier);

                if (document == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show();
                        finish();
                    });

                    return;
                }

                temporaryPdf = cryptographic.decodeFile(this, document.data);
                openPdf();

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to open PDF", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void openPdf() {
        try {
            ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(temporaryPdf, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(descriptor);
            pageCount = pdfRenderer.getPageCount();

            currentPageIndex = 0;
            // Display first page
            renderPage(currentPageIndex);

        } catch (Exception e) {
            Toast.makeText(this, "Could not open PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void renderPage(int pageIndex) {

        databaseExecutor.execute(() -> {
            try {
                if (pdfRenderer == null) {
                    return;
                }

                // Close previous page
                if (currentPage != null) {
                    currentPage.close();
                    currentPage = null;
                }

                // Open requested page
                currentPage = pdfRenderer.openPage(pageIndex);

                int width = currentPage.getWidth();
                int height = currentPage.getHeight();

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                // Render page
                currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                runOnUiThread(() -> {
                    pdfPage.setImageBitmap(bitmap);
                    text_page_number.setText("Page " + (pageIndex + 1) + " / " + pageCount);

                    button_previous.setEnabled(pageIndex > 0);
                    button_next.setEnabled(pageIndex < pageCount - 1);
                });


            } catch (Exception e) {
                Toast.makeText(this, "Page rendering failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}