package com.gil_shiran_or.keepon.trainee.my_friends;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.my_trainers.Review;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFriendTrainerReviewsListAdapter extends RecyclerView.Adapter<MyFriendTrainerReviewsListAdapter.MyTrainerReviewsViewHolder> {

    private List<Review> mReviewsList = new ArrayList<>();
    private DatabaseReference mDatabaseReviewsReference;
    private ChildEventListener mChildEventListener;
    private String mTrainerId;

    public static class MyTrainerReviewsViewHolder extends RecyclerView.ViewHolder {

        public RatingBar ratingBar;
        public TextView reviewTextView;

        public MyTrainerReviewsViewHolder(View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.my_trainer_review_item_rating);
            reviewTextView = itemView.findViewById(R.id.my_trainer_review_item_review);
        }
    }

    public MyFriendTrainerReviewsListAdapter(String trainerId) {
        mTrainerId = trainerId;

        mDatabaseReviewsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId + "/Rating/reviews");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mReviewsList.add(dataSnapshot.getValue(Review.class));
                notifyItemInserted(mReviewsList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReviewsReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public MyTrainerReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trainer_review_item, parent, false);

        return new MyTrainerReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyTrainerReviewsViewHolder holder, int position) {
        Review currentReview = mReviewsList.get(position);

        holder.ratingBar.setRating(currentReview.getRating());
        holder.reviewTextView.setText(currentReview.getReview());
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    public void setReviewToFirebase(Review review) {
        String key = mDatabaseReviewsReference.push().getKey();
        Map<String, Object> reviewValues = review.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(key, reviewValues);

        mDatabaseReviewsReference.updateChildren(childUpdates);
    }

    public void cleanUp() {
        mDatabaseReviewsReference.removeEventListener(mChildEventListener);
    }
}
