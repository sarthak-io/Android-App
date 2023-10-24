package com.example.evault;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewerActivity extends AppCompatActivity {
    private static final String ARG_IMAGE_URL = "image_url";
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        if (getIntent().hasExtra(ARG_IMAGE_URL)) {
            imageUrl = getIntent().getStringExtra(ARG_IMAGE_URL);
        }

        PhotoView photoView = findViewById(R.id.photo_view);
        Picasso.get().load(imageUrl).into(photoView);
    }
}
