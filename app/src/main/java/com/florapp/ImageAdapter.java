package com.florapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageAdapter extends PagerAdapter {
    private ArrayList<String> imageList;
    private HashMap<String, Bitmap> bitmapList;

    public ImageAdapter(ArrayList<String> imageList) {
        this.bitmapList = new HashMap<>();
        this.imageList = imageList;
    }

    public ImageAdapter(ArrayList<String> imageList, HashMap<String,Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
        //this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        final Context context = container.getContext();
        ImageView imageView = new ImageView(context);
        if(context.toString().contains("FloraActivity")) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullImage.class);
                    intent.putStringArrayListExtra("imageList", imageList);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }

        if(bitmapList.containsKey(imageList.get(position))) {
            imageView.setImageBitmap(bitmapList.get(imageList.get(position)));
            container.addView(imageView, 0);
        }
        else {
            new DownloadImage(imageView).execute(imageList.get(position));
            container.addView(imageView, 0);
        }

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        DownloadImage(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(url[0]).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmapList.put(url[0], image);
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
