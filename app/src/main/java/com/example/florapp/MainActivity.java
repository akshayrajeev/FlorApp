package com.example.florapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView iv_logo;
    TextView tv_logo, tv_complete;
    EditText et_search;
    RadioButton rb_scientific, rb_common;
    Switch sw_complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_logo = findViewById(R.id.activity_main_iv_logo);
        tv_logo = findViewById(R.id.activity_main_tv_logo);
        tv_complete = findViewById(R.id.activity_main_tv_complete);
        et_search = findViewById(R.id.activity_main_et_search);
        rb_scientific = findViewById(R.id.activity_main_rb_scientific);
        rb_common = findViewById(R.id.activity_main_rb_common);
        sw_complete = findViewById(R.id.activity_main_sw_complete);

        animate();

        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        String plantingDensityIn = preferences.getString("plantingDensity", "None");
        String precipitationIn = preferences.getString("precipitation", "None");
        if(plantingDensityIn.equals("None") && precipitationIn.equals("None")) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("plantingDensity", "acre");
            editor.putString("precipitation", "cm");
            editor.apply();
        }

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    String query = et_search.getText().toString();
                    if(query.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No Input", Toast.LENGTH_LONG).show();
                    }
                    else {
                        inputMethodManager.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                        getLinks(query);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void animate(){
        iv_logo.animate().alpha(1).setDuration(2000);
        tv_logo.animate().alpha(1).setDuration(2000);
        tv_complete.animate().alpha(1).setDuration(2000);
        et_search.animate().alpha(1).setDuration(2000);
        rb_scientific.animate().alpha(1).setDuration(2000);
        rb_common.animate().alpha(1).setDuration(2000);
        sw_complete.animate().alpha(1).setDuration(2000);
    }

    private void getLinks(String query) {
        String URL = "";
        if(rb_scientific.isChecked()) {
            URL = "https://trefle.io/api/plants?token=MU1JQ1Nhbnl0VXVNQmhEaUV4VHNMdz09&scientific_name=" + query;
        }
        else {
            URL = "https://trefle.io/api/plants?token=MU1JQ1Nhbnl0VXVNQmhEaUV4VHNMdz09&common_name=" + query;
        }
        if(!sw_complete.isChecked()) {
            URL += "&complete_data=true";
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() == 0) {
                    Toast.makeText(getApplicationContext(),"No Results Found",Toast.LENGTH_LONG).show();
                }
                else {
                    parseLinks(response);
                }
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
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void parseLinks(JSONArray response) {
        List<Flora> list = new ArrayList<>();
        for(int i=0; i<response.length(); i++) {
            try {
                JSONObject temp = response.getJSONObject(i);
                Flora flora = new Flora();
                flora.setId(temp.getInt("id"));
                flora.setCompleteData(temp.getBoolean("complete_data"));
                String temp_var = temp.getString("link");
                if(!temp_var.contains("https") && temp_var.contains("http")) {
                    temp_var = temp_var.replace("http", "https");
                }
                flora.setLink(temp_var);
                temp_var = temp.getString("common_name");
                flora.setCommonName(temp_var.substring(0, 1).toUpperCase() + temp_var.substring(1));
                flora.setScientificName(temp.getString("scientific_name"));
                list.add(flora);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        Bundle bundle = new Bundle();
        Intent intent;
        if(list.size() == 1) {
            intent = new Intent(getApplicationContext(), FloraActivity.class);
            bundle.putSerializable("Data", list.get(0));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
