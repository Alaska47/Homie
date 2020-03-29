package com.example.homie.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homie.R;
import com.example.homie.utils.RVAdapterStory;
import com.example.homie.utils.StoryCard;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;


public class RecommendedPostsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static String TAG = "RecommendedPostsFragment";

    private List<StoryCard> storyCards;
    private RecyclerView rv;
    private RVAdapterStory adapter;

    public RecommendedPostsFragment() {}

    public static RecommendedPostsFragment newInstance(String param1, String param2) {
        RecommendedPostsFragment fragment = new RecommendedPostsFragment();
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
        View v = inflater.inflate(R.layout.fragment_recommended_posts, container, false);

        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        storyCards = new ArrayList<>();

        initializeAdapter();
        initializeData();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initializeAdapter() {
        adapter = new RVAdapterStory(storyCards, getActivity());
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        storyCards.add(new StoryCard(BitmapFactory.decodeResource(getResources(), R.drawable.default_cardview_pic), getResources().getString(R.string.default_cv_name), getResources().getString(R.string.default_cv_description), 10000, 12000, 45, 32));
        storyCards.add(new StoryCard(BitmapFactory.decodeResource(getResources(), R.drawable.default_cardview_pic2), "John", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Et nemo nimium beatus est; Dulce amarum, leve asperum, prope longe, stare movere, quadratum rotundum.", 412, 1000, 12, 3233));
        storyCards.add(new StoryCard(BitmapFactory.decodeResource(getResources(), R.drawable.default_cardview_pic), getResources().getString(R.string.default_cv_name), getResources().getString(R.string.default_cv_description), 2123, 8000, 4500, 0));
        initializeAdapter();
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
}
