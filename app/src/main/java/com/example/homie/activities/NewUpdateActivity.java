package com.example.homie.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.homie.R;

public class NewUpdateActivity extends AppCompatActivity {

    private Button postButton;
    private EditText updateTitleText;
    private EditText bioEditText;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_update);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Post an Update");

        postButton = (Button) findViewById(R.id.postUpdateButton);
        updateTitleText = (EditText) findViewById(R.id.updateTitleText);
        bioEditText = (EditText) findViewById(R.id.bioEditText);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
