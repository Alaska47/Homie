package com.example.homie.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homie.R;
import com.example.homie.utils.BackendUtils;
import com.example.homie.utils.DataStorage;
import com.example.homie.utils.DonationRow;
import com.example.homie.utils.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

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

        final String username = new DataStorage(this).getData("username");
        final Context context = getApplicationContext();
        final Activity activity = this;

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = updateTitleText.getText().toString();
                final String description = bioEditText.getText().toString();

                BackendUtils.doGetRequest("/api/createUpdate", new HashMap<String, String>() {{
                    put("username", username);
                    put("title", title);
                    put("description", description);
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                        Toast.makeText(context, "Submitted update!", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                    }
                }, context, activity);
            }
        });


    }
}
