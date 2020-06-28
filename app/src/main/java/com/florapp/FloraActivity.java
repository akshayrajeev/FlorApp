package com.florapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FloraActivity extends AppCompatActivity {
    AdView adView;
    LoadingDialog loadingDialog;

    static {
        System.loadLibrary("native-lib");
    }
    public native String getAPIKey();

    Flora flora;
    HashMap<String,String> growth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        loadingDialog = new LoadingDialog(FloraActivity.this);
        loadingDialog.startLoading();
        ImageView iv_warning = findViewById(R.id.activity_flora_iv_warning);
        TextView tv_warning = findViewById(R.id.activity_flora_tv_warning);
        iv_warning.setVisibility(View.INVISIBLE);
        tv_warning.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        flora = intent.getParcelableExtra("Data");
        getData(flora.getLink());
    }

    private void getData(String link) {
        String token = new String(Base64.decode(getAPIKey(),Base64.DEFAULT));
        link += "?token=" + token;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String error_message = "";
                if(error instanceof TimeoutError) {
                    error_message = "Connection Timeout";
                }
                else if(error instanceof NetworkError) {
                    error_message = "Cannot connect to Internet";
                }
                Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_LONG).show();
                loadingDialog.dismissLoading();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void parseData(JSONObject jsonObject) {
        try{
            JSONObject mainSpecies = jsonObject.getJSONObject("main_species");

            JSONObject family = jsonObject.getJSONObject("family");
            JSONArray images = jsonObject.getJSONArray("images");
            JSONObject foliage = mainSpecies.getJSONObject("foliage");
            JSONObject seedFruit = mainSpecies.getJSONObject("fruit_or_seed");
            JSONObject growth = mainSpecies.getJSONObject("growth");
            JSONObject seed = mainSpecies.getJSONObject("seed");
            JSONObject specification = mainSpecies.getJSONObject("specifications");

            flora.setFamilyCommonName(family.getString("common_name"));
            flora.setFamilyScientificName(family.getString("name"));
            flora.setFlowerColour(mainSpecies.getJSONObject("flower").getString("color"));
            flora.setFoliage(foliage.getString("color"), foliage.getString("porosity_summer"), foliage.getString("porosity_winter"), foliage.getString("texture"));
            flora.setSeedFruit(seedFruit.getString("color"), seedFruit.getString("seed_period_begin"), seedFruit.getString("seed_period_end"), seed.getString("bloom_period"), seed.getString("commercial_availability"), seed.getString("seedling_vigor"), seed.optDouble("seeds_per_pound",0));
            flora.setGrowth(growth.getString("caco_3_tolerance"), growth.getString("drought_tolerance"), growth.getString("fire_tolerance"), growth.getString("ph_maximum"), growth.getString("ph_minimum"), growth.getJSONObject("planting_density_maximum"), growth.getJSONObject("planting_density_minimum"), growth.getJSONObject("precipitation_maximum"), growth.getJSONObject("precipitation_minimum"), growth.getString("fertility_requirement"), growth.getString("moisture_use"), specification.getJSONObject("mature_height"));
            flora.setImages(images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayData();
    }

    private void displayData() {
        ViewPager vp_previewPager = findViewById(R.id.activity_flora_vp_preview);
        TextView tv_common = findViewById(R.id.activity_flora_tv_common);
        TextView tv_scientific = findViewById(R.id.activity_flora_tv_scientific);
        TextView tv_familyCommon = findViewById(R.id.activity_flora_tv_familyCommon);
        TextView tv_familyScientific = findViewById(R.id.activity_flora_tv_familyScientific);
        TextView tv_foliageColor = findViewById(R.id.activity_flora_tv_foliageColor);
        TextView tv_summerPorosity = findViewById(R.id.activity_flora_tv_foliageSummer);
        TextView tv_winterPorosity = findViewById(R.id.activity_flora_tv_foliageWinter);
        TextView tv_foliageTexture = findViewById(R.id.activity_flora_tv_foliageTexture);
        TextView tv_seedFruitColor = findViewById(R.id.activity_flora_tv_seedFruitColor);
        TextView tv_seedFruitSeedBegin = findViewById(R.id.activity_flora_tv_seedFruitSeedBegin);
        TextView tv_seedFruitSeedEnd = findViewById(R.id.activity_flora_tv_seedFruitSeedEnd);
        TextView tv_seedFruitBloomPeriod = findViewById(R.id.activity_flora_tv_seedFruitBloomPeriod);
        TextView tv_seedFruitCommercialAvailability = findViewById(R.id.activity_flora_tv_seedFruitCommercialAvailability);
        TextView tv_seedFruitSeedlingVigor = findViewById(R.id.activity_flora_tv_seedFruitSeedlingVigor);
        TextView tv_seedFruitSeedPerPound = findViewById(R.id.activity_flora_tv_seedFruitSeedsPerPound);
        TextView tv_caco3Tolerance = findViewById(R.id.activity_flora_tv_caco3Tolerance);
        TextView tv_droughtTolerance = findViewById(R.id.activity_flora_tv_droughtTolerance);
        TextView tv_fireTolerance = findViewById(R.id.activity_flora_tv_fireTolerance);
        TextView tv_minPh = findViewById(R.id.activity_flora_tv_minPh);
        TextView tv_maxPh = findViewById(R.id.activity_flora_tv_maxPh);
        TextView tv_fertilityRequirement = findViewById(R.id.activity_flora_tv_fertilityRequirement);
        TextView tv_moistureUse = findViewById(R.id.activity_flora_tv_moistureUse);

        if(flora.getImages().size() > 0) {
            ImageAdapter imageAdapter = new ImageAdapter(flora.getImages());
            vp_previewPager.setAdapter(imageAdapter);
        }
        else {
            ImageView iv_warning = findViewById(R.id.activity_flora_iv_warning);
            TextView tv_warning = findViewById(R.id.activity_flora_tv_warning);
            iv_warning.setVisibility(View.VISIBLE);
            tv_warning.setVisibility(View.VISIBLE);
        }

        HashMap<String,String> foliage = flora.getFoliage();
        HashMap<String,String> seedFruit = flora.getSeedFruit();
        growth = flora.getGrowth();

        loadingDialog.dismissLoading();

        tv_common.setText(flora.getCommonName());
        tv_scientific.setText(flora.getScientificName());
        tv_familyCommon.setText(flora.getFamilyCommonName());
        tv_familyScientific.setText(flora.getFamilyScientificName());
        tv_foliageColor.setText(foliage.get("color"));
        tv_summerPorosity.setText(foliage.get("porositySummer"));
        tv_winterPorosity.setText(foliage.get("porosityWinter"));
        tv_foliageTexture.setText(foliage.get("texture"));
        tv_seedFruitColor.setText(seedFruit.get("color"));
        tv_seedFruitSeedBegin.setText(seedFruit.get("seedPeriodBegin"));
        tv_seedFruitSeedEnd.setText(seedFruit.get("seedPeriodEnd"));
        tv_seedFruitBloomPeriod.setText(seedFruit.get("bloomPeriod"));
        tv_seedFruitCommercialAvailability.setText(seedFruit.get("commercialAvailability"));
        tv_seedFruitSeedlingVigor.setText(seedFruit.get("seedlingVigor"));
        tv_seedFruitSeedPerPound.setText(seedFruit.get("seedsPerPound"));
        tv_caco3Tolerance.setText(growth.get("caco3Tolerance"));
        tv_droughtTolerance.setText(growth.get("droughtTolerance"));
        tv_fireTolerance.setText(growth.get("fireTolerance"));
        tv_minPh.setText(growth.get("minPh"));
        tv_maxPh.setText(growth.get("maxPh"));
        tv_fertilityRequirement.setText(growth.get("fertilityRequirement"));
        tv_moistureUse.setText(growth.get("moistureUse"));
        updateTextView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_settings:
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;
        }
        return false;
    }

    void updateTextView() {
        TextView tv_minPlantingDensity = findViewById(R.id.activity_flora_tv_minPlantingDensity);
        TextView tv_maxPlantingDensity = findViewById(R.id.activity_flora_tv_maxPlantingDensity);
        TextView tv_minPrecipitation = findViewById(R.id.activity_flora_tv_minPrecipitation);
        TextView tv_maxPrecipitation = findViewById(R.id.activity_flora_tv_maxPrecipitation);
        TextView tv_matureHeight = findViewById(R.id.activity_flora_tv_matureHeight);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String plantingDensityIn = preferences.getString("plantingDensity", "None");
        String precipitationIn = preferences.getString("precipitation", "None");
        String matureHeightIn = preferences.getString("matureHeight", "None");
        SpannableString spannableString;
        int size = (int)tv_matureHeight.getTextSize() - 3;

        if(plantingDensityIn.equals("acre")) {
            spannableString = new SpannableString(" acre");
            tv_minPlantingDensity.setText(growth.get("minPlantingDensityAcre"));
            tv_maxPlantingDensity.setText(growth.get("maxPlantingDensityAcre"));
        }
        else {
            spannableString = new SpannableString(" sqm");
            tv_minPlantingDensity.setText(growth.get("minPlantingDensitySqm"));
            tv_maxPlantingDensity.setText(growth.get("maxPlantingDensitySqm"));
        }
        spannableString.setSpan(new AbsoluteSizeSpan(size, false), 1, spannableString.length(), 0);
        if(!tv_maxPlantingDensity.getText().equals("No Data")) {
            tv_maxPlantingDensity.append(spannableString);
        }
        if(!tv_minPlantingDensity.getText().equals("No Data")) {
            tv_minPlantingDensity.append(spannableString);
        }

        if(precipitationIn.equals("cm")) {
            spannableString = new SpannableString(" cm");
            tv_minPrecipitation.setText(growth.get("minPrecipitationCm"));
            tv_maxPrecipitation.setText(growth.get("maxPrecipitationCm"));
        }
        else {
            spannableString = new SpannableString(" inch");
            tv_minPrecipitation.setText(growth.get("minPrecipitationInch"));
            tv_maxPrecipitation.setText(growth.get("maxPrecipitationInch"));
        }
        spannableString.setSpan(new AbsoluteSizeSpan(size, false), 1, spannableString.length(), 0);
        if(!tv_minPrecipitation.getText().equals("No Data")) {
            tv_minPrecipitation.append(spannableString);
        }
        if(!tv_maxPrecipitation.getText().equals("No Data")) {
            tv_maxPrecipitation.append(spannableString);
        }

        if(matureHeightIn.equals("cm")) {
            spannableString = new SpannableString(" cm");
            tv_matureHeight.setText(growth.get("matureHeightCm"));
        }
        else {
            spannableString = new SpannableString(" ft");
            tv_matureHeight.setText(growth.get("matureHeightFt"));
        }
        spannableString.setSpan(new AbsoluteSizeSpan(size, false), 1, spannableString.length(), 0);
        if(!tv_matureHeight.getText().equals("No Data")) {
            tv_matureHeight.append(spannableString);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateTextView();
    }
}
