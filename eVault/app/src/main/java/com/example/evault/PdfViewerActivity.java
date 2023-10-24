package com.example.evault;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class PdfViewerActivity extends AppCompatActivity {
    private static final String ARG_PDF_URL = "pdf_url";
    private String pdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        if (getIntent().hasExtra(ARG_PDF_URL)) {
            pdfUrl = getIntent().getStringExtra(ARG_PDF_URL);
        }

        WebView webView = findViewById(R.id.web_view_pdf);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdfUrl);
    }
}
