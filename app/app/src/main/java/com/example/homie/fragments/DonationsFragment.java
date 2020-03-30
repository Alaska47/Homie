package com.example.homie.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.example.homie.R;
import com.example.homie.utils.BackendUtils;
import com.example.homie.utils.DataStorage;
import com.example.homie.utils.DonationRow;
import com.example.homie.utils.RVAdapterDonation;
import com.example.homie.utils.StoryCard;
import com.example.homie.utils.VolleyCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class DonationsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<DonationRow> donationRowList;
    private RecyclerView rv;
    private RVAdapterDonation adapter;

    public DonationsFragment() {
        // Required empty public constructor
    }

    public static DonationsFragment newInstance(String param1, String param2) {
        DonationsFragment fragment = new DonationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_donations, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 101);
        }

        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        donationRowList = new ArrayList<>();

        final String username = new DataStorage(getActivity()).getData("username");

        BackendUtils.doGetRequest("/api/getDonations", new HashMap<String, String>() {{
            put("username", username);
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, result);
                try {
                    JSONArray jArray = new JSONArray(result);
                    for(int i = 0; i < jArray.length(); i++) {
                        JSONObject object = jArray.getJSONObject(i);
                        String amount = object.getString("amount");
                        String description = object.getString("description");
                        String donatorId = object.getString("donatorId");
                        String donatorPhone = object.getString("donatorPhone");
                        String encodedImage = object.getString("picture");
                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        donationRowList.add(new DonationRow(decodedByte, donatorId, description, Integer.parseInt(amount), donatorPhone, true));
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

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        adapter = new RVAdapterDonation(donationRowList, getActivity());
        rv.setAdapter(adapter);
    }

    private void initializeData() {

//        donationRowList.add(new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.anoos), "Kash", "For spending on food and clothing", 20, false));
//        donationRowList.add(new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon), "Sid", "To pay phone bills", 0, false));
//        donationRowList.add(new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon), "John", "To pay for a nice haircut", 10, false));
    /**
        BackendUtils.doGetRequest("/api/getHomeless", new HashMap<String, String>() {{
        }}, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, result);
                try {
                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++){
                        JSONObject object = jArray.getJSONObject(i);

                        DonationRow d = new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon), object.getString("firstName"), object.getString("description"), object.getInt("moneyRaised"), object.getInt("goal"), object.getInt("numLikes"), object.getInt("score"));
                        Log.d("Name: ", object.getString("firstName"));
                        donationRowList.add(d);

                    }
                    initializeAdapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {
                Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
            }
        }, getActivity(), getActivity());
     **/
        initializeAdapter();
    }
    private void getDonos(){
        /**
         * Hashmap needs to have username
         BackendUtils.doGetRequest("/api/getDonations/", new HashMap<String, String>() {{
         }}, new VolleyCallback() {
        @Override
        public void onSuccess(String result) {
        Log.d(TAG, result);
        }

        @Override
        public void onError(VolleyError error) {
        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
        }
        }, getActivity(), getActivity());
         **/
    }
}
