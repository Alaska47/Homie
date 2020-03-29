package com.example.homie.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogBehavior;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.homie.R;
import com.example.homie.utils.RVAdapterStory;
import com.example.homie.utils.StoryCard;
import com.example.homie.utils.UpdateCard;
import com.example.homie.utils.UpdatesTimelineAdapter;
import com.google.android.material.appbar.AppBarLayout;
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

public class ViewProfileActivity extends AppCompatActivity {

    CollapsingToolbarLayout ctl;

    RecyclerView recyclerView;
    UpdatesTimelineAdapter updatesTimelineAdapter;
    LinearLayoutManager llm;
    List<UpdateCard> updateModelList;


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

        MaterialButton donateButton = (MaterialButton) findViewById(R.id.donate_button);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You just donated!", Toast.LENGTH_LONG).show();
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
}
