package com.example.homie.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.homie.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class UpdatesTimelineAdapter extends RecyclerView.Adapter<UpdatesTimelineAdapter.TimelineViewHolder> {

    public static class TimelineViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView title;
        TextView description;
        ImageButton upvote;
        ImageButton downvote;

        TimelineViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.text_timeline_date);
            title = (TextView) itemView.findViewById(R.id.text_timeline_title);
            description = (TextView) itemView.findViewById(R.id.text_timeline_description);
            upvote = (ImageButton) itemView.findViewById(R.id.upvote_update);
            downvote = (ImageButton) itemView.findViewById(R.id.downvote_update);
        }
    }

    List<UpdateCard> updates;
    Context a;
    Activity b;

    public UpdatesTimelineAdapter(List<UpdateCard> updates, Context a, Activity b){
        this.updates = updates;
        this.a = a;
        this.b = b;
    }

    @Override
    public int getItemCount() {
        if(updates == null)
            return 0;
        return updates.size();
    }

    public void clear() {
        updates.clear();
        notifyDataSetChanged();
    }

    @Override
    public UpdatesTimelineAdapter.TimelineViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_timeline, viewGroup, false);
        UpdatesTimelineAdapter.TimelineViewHolder pvh = new UpdatesTimelineAdapter.TimelineViewHolder(v);
        return pvh;
    }

    @Override
    public int getItemViewType(int i) {
        return TimelineView.getTimeLineViewType(i, getItemCount());
    }

    @Override
    public void onBindViewHolder(UpdatesTimelineAdapter.TimelineViewHolder timelineViewHolder, int i) {
        timelineViewHolder.date.setText(updates.get(i).date);
        timelineViewHolder.title.setText(updates.get(i).title);
        timelineViewHolder.description.setText(updates.get(i).description);
        final Context a = this.a;
        final String username = updates.get(i).userId;
        timelineViewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendUtils.doGetRequest("/api/upvote", new HashMap<String, String>() {{
                    put("username", username);
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                    }
                }, a, b);
                Toast toast = Toast.makeText(a,"Upvoted!",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        timelineViewHolder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendUtils.doGetRequest("/api/downvote", new HashMap<String, String>() {{
                    put("username", username);
                }}, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, result);
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                    }
                }, a, b);
                Toast toast = Toast.makeText(a, "Downvoted!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
