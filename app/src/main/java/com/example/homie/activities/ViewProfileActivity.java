package com.example.homie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.homie.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ViewProfileActivity extends AppCompatActivity {

    CollapsingToolbarLayout ctl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Their Story...");
        setSupportActionBar(mToolbar);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_cardview_pic);

        ImageView im = (ImageView) findViewById(R.id.expandedImage);
        im.setImageBitmap(bitmap);


    }
}
