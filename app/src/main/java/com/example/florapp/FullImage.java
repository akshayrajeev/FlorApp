package com.example.florapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FullImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final ArrayList<String> imageList = intent.getStringArrayListExtra("imageList");
        int position = intent.getIntExtra("position", 0);
        ViewPager vp_fullImage = findViewById(R.id.activity_full_image_vp_fullImage);
        final TextView tv_pageNumber = findViewById(R.id.activity_full_image_pageNumber);
        ImageAdapter imageAdapter = new ImageAdapter(imageList);
        vp_fullImage.setAdapter(imageAdapter);
        vp_fullImage.setCurrentItem(position);

        vp_fullImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv_pageNumber.setText(position+1 + "/" + imageList.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
