package com.gil_shiran_or.keepon.trainee.ui.ratings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;

import java.util.List;

public class OnlineRatingsListAdapter extends RecyclerView.Adapter<OnlineRatingsListAdapter.MyRatingsViewHolder> {

    private List<MyRatingsItem> myRatingsList;
    private Context context;

    public static class MyRatingsViewHolder extends RecyclerView.ViewHolder {

        public TextView trainerNameTextView;
        public RatingBar trainerRatingBar;
        public TextView ratingStateTextView;
        public EditText reviewEditText;
        public Button sendButton;

        public MyRatingsViewHolder(View itemView) {
            super(itemView);
            trainerNameTextView = itemView.findViewById(R.id.my_ratings_item_trainer_name);
            trainerRatingBar = itemView.findViewById(R.id.my_ratings_item_rating);
            ratingStateTextView = itemView.findViewById(R.id.my_ratings_item_rating_state);
            reviewEditText = itemView.findViewById(R.id.my_ratings_item_review);
            sendButton = itemView.findViewById(R.id.my_ratings_item_send_button);
        }
    }

    public OnlineRatingsListAdapter(List<MyRatingsItem> myRatingsList, Context context) {
        this.myRatingsList = myRatingsList;
        this.context = context;
    }

    @Override
    public MyRatingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ratings_item, parent, false);

        return new MyRatingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyRatingsViewHolder holder, int position) {
        MyRatingsItem currentMyRatingsItem = myRatingsList.get(position);

        holder.trainerNameTextView.setText(currentMyRatingsItem.getTrainerName());
        holder.trainerRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float rating = ratingBar.getRating();

                holder.sendButton.setEnabled(true);

                if (rating <= 1) {
                    holder.ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_very_bad));
                }
                else if (rating <= 2) {
                    holder.ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_need_improvement));
                }
                else if (rating <= 3) {
                    holder.ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_good));
                }
                else if (rating <= 4) {
                    holder.ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_great));
                }
                else {
                    holder.ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_awesome));
                }
            }
        });

        holder.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.trainerRatingBar.setIsIndicator(true);
                holder.reviewEditText.setVisibility(View.GONE);
                holder.sendButton.setVisibility(View.GONE);

                Toast.makeText(context, "Rating sent successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myRatingsList.size();
    }
}
