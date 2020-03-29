package com.example.homie.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.homie.R;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapterDonation extends RecyclerView.Adapter<RVAdapterDonation.DonationViewHolder>{
    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView backgroundImage;
        TextView name;
        TextView description;
        ProgressBar moneyRaisedProgress;
        TextView moneyRaisedDescription;
        TextView numLikes;
        TextView numKarma;

        DonationViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            backgroundImage = (ImageView) itemView.findViewById(R.id.cv_background_image);
            name = (TextView) itemView.findViewById(R.id.cv_name);
            description = (TextView) itemView.findViewById(R.id.cv_description);
            moneyRaisedProgress = (ProgressBar) itemView.findViewById(R.id.cv_raised_money);
            moneyRaisedDescription = (TextView) itemView.findViewById(R.id.cv_raised_money_description);
            numLikes = (TextView) itemView.findViewById(R.id.cv_num_likes);
            numKarma = (TextView) itemView.findViewById(R.id.cv_num_karma);
        }
    }

    List<StoryCard> storyCards;

    public RVAdapterDonation(List<StoryCard> stories){
        this.storyCards = stories;
    }

    @Override
    public int getItemCount() {
        if(storyCards == null)
            return 0;
        return storyCards.size();
    }

    public void clear() {
        storyCards.clear();
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
        storyViewHolder.backgroundImage.setImageBitmap(storyCards.get(i).backgroundImage);
        storyViewHolder.name.setText(String.format("Help out %s", storyCards.get(i).name));
        storyViewHolder.description.setText(storyCards.get(i).description);
        storyViewHolder.moneyRaisedDescription.setText(String.format("$%,d raised of $%,d goal", storyCards.get(i).moneyRaised, storyCards.get(i).moneyRaisedGoal));
        storyViewHolder.moneyRaisedProgress.setProgress((int) (storyCards.get(i).moneyRaised * 100.0 / (storyCards.get(i).moneyRaisedGoal)));
        storyViewHolder.numLikes.setText(String.format("%s", format(storyCards.get(i).numLikes)));
        storyViewHolder.numKarma.setText(String.format("%s", format(storyCards.get(i).numKarma)));
    }

    private String format(int num) {
        if (num >= 100000)
            return format(num / 1000) + "k";
        if (num >= 1000) {
            return String.format("%.1fk", num / 1000D);
        }
        return String.format("%,d", num);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}