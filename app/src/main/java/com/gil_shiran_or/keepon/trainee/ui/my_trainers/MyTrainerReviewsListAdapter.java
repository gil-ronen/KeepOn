package com.gil_shiran_or.keepon.trainee.ui.my_trainers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.util.List;

public class MyTrainerReviewsListAdapter extends RecyclerView.Adapter<MyTrainerReviewsListAdapter.MyTrainerReviewsViewHolder> {

    private List<MyTrainerReviewItem> myTrainerReviewsList;

    public static class MyTrainerReviewsViewHolder extends RecyclerView.ViewHolder {

        public RatingBar ratingBar;
        public TextView reviewTextView;

        public MyTrainerReviewsViewHolder(View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.my_trainer_review_item_rating);
            reviewTextView = itemView.findViewById(R.id.my_trainer_review_item_review);
        }
    }

    public MyTrainerReviewsListAdapter(List<MyTrainerReviewItem> myTrainerReviewsList) {
        this.myTrainerReviewsList = myTrainerReviewsList;
    }

    @Override
    public MyTrainerReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trainer_review_item, parent, false);

        return new MyTrainerReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyTrainerReviewsViewHolder holder, int position) {
        MyTrainerReviewItem currentMyTrainerReviewItem = myTrainerReviewsList.get(position);

        holder.ratingBar.setRating(currentMyTrainerReviewItem.getRating());
        holder.reviewTextView.setText(currentMyTrainerReviewItem.getReview());
    }

    @Override
    public int getItemCount() {
        return myTrainerReviewsList.size();
    }
}
