package com.example.homie.activities;

import android.app.AppComponentFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.example.homie.R;
import com.example.homie.fragments.DonationsFragment;
import com.example.homie.fragments.HighlightedPostsFragment;
import com.example.homie.fragments.ProfileFragment;
import com.example.homie.fragments.RecommendedPostsFragment;
import com.example.homie.utils.DataStorage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity implements HighlightedPostsFragment.OnFragmentInteractionListener, RecommendedPostsFragment.OnFragmentInteractionListener, DonationsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    BottomNavigationView bottomNavigation;
    ActionBar actionBar;
    public String TAG = "HomeActivity";
    public final String USERNAME = "Nerja";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DataStorage(this).storeData("username", USERNAME, false);

        actionBar = getSupportActionBar();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.recommended_posts:
                                openFragment(RecommendedPostsFragment.newInstance("", ""));
                                actionBar.setTitle("Recommended Stories");
                                return true;
                            case R.id.highlighted_posts:
                                openFragment(HighlightedPostsFragment.newInstance("", ""));
                                actionBar.setTitle("Stories We Like");
                                return true;
                            case R.id.donations:
                                openFragment(DonationsFragment.newInstance("", ""));
                                actionBar.setTitle("Donations");
                                return true;
                            case R.id.profile:
                                openFragment(ProfileFragment.newInstance("", ""));
                                actionBar.setTitle("My Profile");
                                return true;
                        }
                        return false;
                    }
                };
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigation.setSelectedItemId(R.id.recommended_posts);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}
