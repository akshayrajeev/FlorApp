package com.example.florapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class FloraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Flora flora = (Flora) bundle.getSerializable("Data");

        Toast.makeText(getApplicationContext(), flora.getStringData(), Toast.LENGTH_LONG).show();
    }
}
