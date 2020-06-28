package com.florapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RadioButton rb_acre = findViewById(R.id.activity_setting_rb_acre);
        RadioButton rb_sqm = findViewById(R.id.activity_setting_rb_sqm);
        RadioButton rb_inch = findViewById(R.id.activity_setting_rb_inch);
        RadioButton rb_cm = findViewById(R.id.activity_setting_rb_cm);
        RadioButton rb_matureHeightCm = findViewById(R.id.activity_setting_rb_matureHeightCm);
        RadioButton rb_matureHeightFt = findViewById(R.id.activity_setting_rb_matureHeightFt);
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String plantingDensityIn = preferences.getString("plantingDensity", "None");
        String precipitationIn = preferences.getString("precipitation", "None");
        String matureHeightIn = preferences.getString("matureHeight", "None");

        if(plantingDensityIn.equals("acre")) {
            rb_acre.setChecked(true);
        }
        else if(plantingDensityIn.equals("sqm")) {
            rb_sqm.setChecked(true);
        }

        if(precipitationIn.equals("inch")) {
            rb_inch.setChecked(true);
        }
        else if(precipitationIn.equals("cm")) {
            rb_cm.setChecked(true);
        }

        if(matureHeightIn.equals("cm")) {
            rb_matureHeightCm.setChecked(true);
        }
        else if(matureHeightIn.equals("ft")) {
            rb_matureHeightFt.setChecked(true);
        }
    }

    public void onRadioButtonClicked(View view) {
        editor = preferences.edit();
        switch (view.getId()) {
            case R.id.activity_setting_rb_acre:
                editor.putString("plantingDensity","acre");
                break;
            case R.id.activity_setting_rb_sqm:
                editor.putString("plantingDensity","sqm");
                break;
            case  R.id.activity_setting_rb_inch:
                editor.putString("precipitation","inch");
                break;
            case R.id.activity_setting_rb_cm:
                editor.putString("precipitation","cm");
                break;
            case R.id.activity_setting_rb_matureHeightCm:
                editor.putString("matureHeight","cm");
                break;
            case R.id.activity_setting_rb_matureHeightFt:
                editor.putString("matureHeight","ft");
                break;
        }
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
