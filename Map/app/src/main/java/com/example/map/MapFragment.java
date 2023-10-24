package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;

public class MapFragment extends Fragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        webView = view.findViewById(R.id.webView);

        // Retrieve the map URL from the arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            String mapUrl = bundle.getString("mapUrl");
            if (mapUrl != null && !mapUrl.isEmpty()) {
                setupWebView(mapUrl);
            }
        }

        return view;
    }

    private void setupWebView(String mapUrl) {
        webView.loadUrl(mapUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }
}