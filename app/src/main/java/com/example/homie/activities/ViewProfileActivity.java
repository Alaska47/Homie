package com.example.homie.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homie.R;

import com.example.homie.utils.UpdateCard;
import com.example.homie.utils.UpdatesTimelineAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.homie.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class ViewProfileActivity extends AppCompatActivity {

    CollapsingToolbarLayout ctl;

    RecyclerView recyclerView;
    UpdatesTimelineAdapter updatesTimelineAdapter;
    LinearLayoutManager llm;
    List<UpdateCard> updateModelList;

    private Menu menu;

    boolean selectedHeart = false;

    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);

    Button donateButton;
    EditText amount;

    String amountNum ="";

    Drawable unfilledHeart;
    Drawable filledHeart;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.view_profile_menu, menu);

        filledHeart = AppCompatResources.getDrawable(this, R.drawable.ic_favorite_white_24dp);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_favorite_border_black_24dp);
        unfilledHeart = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(unfilledHeart, Color.WHITE);
        menu.getItem(0).setIcon(unfilledHeart);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // extract user_id from intent

        // get data
        String name = getResources().getString(R.string.default_cv_name);
        // decode bitmap from base64
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_cardview_pic);
        String firstName = name.split(" ")[0] + "'s";
        int raisedMoney = 120;
        int raisedMoneyGoal = 500;
        final String youtubeId = "adzmLBKMsfA";
        String description = getResources().getString(R.string.default_cv_description);
        // also get a time stamp for each update
        String[][] updates = {{"First donations", "I spent the first $20 on some food for the night, I went to a high quality restaurant"}, {"Second donation", "I used $50 to buy myself a suit, I'm tryna get a job at walmart"}, {"Third donation", "I spent another $50 dollars on paying for my last couple phone bills so that I can keep giving you guys updates"}};

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(firstName + " Story...");
        setSupportActionBar(mToolbar);

        ImageView im = (ImageView) findViewById(R.id.expandedImage);
        im.setImageBitmap(bitmap);

        TextView moneyRaisedDescription = (TextView) findViewById(R.id.raised_money_description);
        ProgressBar moneyRaisedProgress = (ProgressBar) findViewById(R.id.raised_money_progress);

        moneyRaisedDescription.setText(String.format("$%,d raised of $%,d goal", raisedMoney, raisedMoneyGoal));
        moneyRaisedProgress.setProgress((int) (raisedMoney * 100.0 / (raisedMoneyGoal)));

        amount = (EditText) findViewById(R.id.amount);
        MaterialButton donateButton = (MaterialButton) findViewById(R.id.donate_button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(youtubeId, 0);
            }
        });

        TextView storyTitle = (TextView) findViewById(R.id.story_title);
        storyTitle.setText(name);

        TextView storyDescription = (TextView) findViewById(R.id.story_description);
        storyDescription.setText(description);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setNestedScrollingEnabled(false);

        updateModelList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        for(int i = updates.length - 1; i >= 0; i--) {
            Calendar a = Calendar.getInstance();
            // sample
            a.set(Calendar.MONTH, i);
            Date c = a.getTime();
            updateModelList.add(new UpdateCard(dateFormat.format(c), updates[i][0], updates[i][1]));
        }
        initializeAdapter();
    }

    private void initializeAdapter() {
        updatesTimelineAdapter = new UpdatesTimelineAdapter(updateModelList, this);
        recyclerView.setAdapter(updatesTimelineAdapter);
    }

    private void processPayment() {
        amountNum = amount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amountNum)), "USD", "Donation", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try{
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amountNum)


                        );

                    } catch (JSONException e ){
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_heart) {
            if(selectedHeart == false) {
                menu.getItem(0).setIcon(filledHeart);
                Toast.makeText(this, "Liked!", Toast.LENGTH_SHORT).show();
                selectedHeart = true;
            } else {
                menu.getItem(0).setIcon(unfilledHeart);
                Toast.makeText(this, "Unliked!", Toast.LENGTH_SHORT).show();
                selectedHeart = false;
            }
            return true;
        }
        return false;
    }
}
