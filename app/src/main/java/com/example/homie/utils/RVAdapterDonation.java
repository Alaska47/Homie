package com.example.homie.utils;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.homie.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapterDonation extends RecyclerView.Adapter<RVAdapterDonation.DonationViewHolder>{
    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfile;
        TextView title;
        TextView description;
        TextView donationMoney;
        ImageButton messageButton;
        ImageButton callButton;
        LinearLayout ll;
        LinearLayout ll2;

        DonationViewHolder(View itemView) {
            super(itemView);
            userProfile = (ImageView) itemView.findViewById(R.id.profile_icon);
            title = (TextView) itemView.findViewById(R.id.donation_title);
            description = (TextView) itemView.findViewById(R.id.donation_description);
            donationMoney = (TextView) itemView.findViewById(R.id.donation_money);
            messageButton = (ImageButton) itemView.findViewById(R.id.message_button);
            callButton = (ImageButton) itemView.findViewById(R.id.call_button);
            ll = (LinearLayout) itemView.findViewById(R.id.money_ll);
            ll2 = (LinearLayout) itemView.findViewById(R.id.call_msg_button_layout);
        }
    }

    List<DonationRow> donations;

    public RVAdapterDonation(List<DonationRow> donations){
        this.donations = donations;
    }

    @Override
    public int getItemCount() {
        if(donations == null)
            return 0;
        return donations.size();
    }

    public void clear() {
        donations.clear();
        notifyDataSetChanged();
    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.donation_row, viewGroup, false);
        DonationViewHolder pvh = new DonationViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(DonationViewHolder donationViewHolder, int i) {
        boolean isDonor = donations.get(i).isDonor;
        donationViewHolder.userProfile.setImageBitmap(donations.get(i).profilePic);
        donationViewHolder.title.setText(String.format("To %s", donations.get(i).donationTitle));
        donationViewHolder.description.setText(donations.get(i).donationDescription);
        donationViewHolder.donationMoney.setText(String.format("$%,d", donations.get(i).donationAmount));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}