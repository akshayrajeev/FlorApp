package com.example.florapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    void startLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.layout_loading, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void dismissLoading() {
        alertDialog.dismiss();
    }
}
