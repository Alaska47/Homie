package com.example.homie.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.homie.R;
import com.example.homie.utils.BackendUtils;
import com.example.homie.utils.RVAdapterStory;
import com.example.homie.utils.StoryCard;
import com.example.homie.utils.VolleyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private boolean loading = false;

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
    public void onResume() {
        super.onResume();
        storyCards = new ArrayList<>();
        initializeData();
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
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        storyCards = new ArrayList<>();
        if(!loading)
            initializeData();
    }

    private void initializeAdapter() {
        adapter = new RVAdapterStory(storyCards, getActivity());
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        storyCards.clear();
        BackendUtils.doGetRequest("/api/getHomeless", new HashMap<String, String>() {{
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, result);
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++){
                        JSONObject object = jArray.getJSONObject(i);
                        String encodedImage = object.getString("picture");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        StoryCard s = new StoryCard(object.getString("userName"), decodedByte, object.getString("firstName"), object.getString("description"), object.getInt("moneyRaised"), object.getInt("goal"), object.getInt("numLikes"), object.getInt("score"));
                        Log.d("Name: ", object.getString("firstName"));
                        storyCards.add(s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initializeAdapter();

            }

            @Override
            public void onError(VolleyError error) {
                Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
            }
        }, getActivity(), getActivity());
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
