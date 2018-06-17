package com.example.inache.atmpt21gph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class Act2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act2);

        String url = getIntent().getStringExtra("url");
        ImageView imageView = findViewById(R.id.overlayGif);
        Glide.with(this).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);

        TextView textView = findViewById(R.id.urlText);
        textView.setText(url);




    }
}
