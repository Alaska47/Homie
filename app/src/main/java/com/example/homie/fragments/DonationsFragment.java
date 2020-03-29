package com.example.homie.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homie.R;
import com.example.homie.utils.DonationRow;
import com.example.homie.utils.RVAdapterDonation;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        donationRowList = new ArrayList<>();

        initializeAdapter();
        initializeData();

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
        adapter = new RVAdapterDonation(donationRowList);
        rv.setAdapter(adapter);
    }

    private void initializeData() {
        donationRowList.add(new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon), "Matt", "For spending on suits", 120, false));
        donationRowList.add(new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon), "Kash", "For spending on food and clothing", 20, false));
        donationRowList.add(new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon), "Sid", "To pay phone bills", 0, false));
        donationRowList.add(new DonationRow(BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon), "John", "To pay for a nice haircut", 10, false));
        initializeAdapter();
    }
}
