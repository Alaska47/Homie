package com.example.homie.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homie.R;
import com.example.homie.utils.DonationRow;
import com.example.homie.utils.RVAdapterDonation;
import com.example.homie.utils.RVAdapterStory;
import com.example.homie.utils.StoryCard;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HighlightedPostsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "HighlightPostsFragment";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<StoryCard> storyCards;
    private RecyclerView rv;
    private RVAdapterStory adapter;

    public HighlightedPostsFragment() {}

    public static HighlightedPostsFragment newInstance(String param1, String param2) {
        HighlightedPostsFragment fragment = new HighlightedPostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: recommended");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_highlighted_posts, container, false);
        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        storyCards = new ArrayList<>();

        initializeAdapter();
        initializeData();

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initializeAdapter() {
        adapter = new RVAdapterStory(storyCards, getActivity());
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        String defaultDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Et nemo nimium beatus est; Dulce amarum, leve asperum, prope longe, stare movere, quadratum rotundum.";
        storyCards.add(new StoryCard("kash",BitmapFactory.decodeResource(getResources(), R.drawable.default_cardview_pic3), "Kash", defaultDescription, 10000, 12000, 45, 32));
        storyCards.add(new StoryCard("kash",BitmapFactory.decodeResource(getResources(), R.drawable.john), "Jonathan", defaultDescription, 412, 1000, 12, 3233));
        storyCards.add(new StoryCard("kash",BitmapFactory.decodeResource(getResources(), R.drawable.lizzy), "Lizzy", defaultDescription, 2123, 8000, 4500, 0));
        initializeAdapter();
    }
}
