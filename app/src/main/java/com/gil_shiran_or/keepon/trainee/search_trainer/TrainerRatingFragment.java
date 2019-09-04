package com.gil_shiran_or.keepon.trainee.search_trainer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.Rating;
import com.gil_shiran_or.keepon.trainee.my_trainers.AddReviewDialog;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainer;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerReviewsListAdapter;
import com.gil_shiran_or.keepon.trainee.my_trainers.Review;
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TrainerRatingFragment extends Fragment {

    private DatabaseReference mDatabaseRatingReference;
    private TrainerReviewsListAdapter mTrainerReviewsListAdapter;
    private ValueEventListener mRatingValueEventListener;
    private String mTrainerId;
    private Rating mRating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainer_rating, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTrainerId = getArguments().getString("trainerId");

        buildReviewsRecyclerView();

        ViewGroup reviewsExpanderViewGroup = getView().findViewById(R.id.trainer_reviews_expander);
        ViewGroup reviewsViewGroup = getView().findViewById(R.id.trainer_reviews_list);

        new ExpandableViewGroup("View reviews", "Hide reviews", reviewsExpanderViewGroup, reviewsViewGroup);

        final TextView trainerNameTextView = getView().findViewById(R.id.trainer_rating_name);
        final RatingBar trainerRatingBar = getView().findViewById(R.id.trainer_rating);
        final TextView trainerRatingScore = getView().findViewById(R.id.trainer_rating_score);
        final ProgressBar oneStarProgressBar = getView().findViewById(R.id.trainer_rating_1_star);
        final TextView oneStarTextView = getView().findViewById(R.id.trainer_rating_1_star_reviewers);
        final ProgressBar twoStarProgressBar = getView().findViewById(R.id.trainer_rating_2_stars);
        final TextView twoStarTextView = getView().findViewById(R.id.trainer_rating_2_stars_reviewers);
        final ProgressBar threeStarProgressBar = getView().findViewById(R.id.trainer_rating_3_stars);
        final TextView threeStarTextView = getView().findViewById(R.id.trainer_rating_3_stars_reviewers);
        final ProgressBar fourStarProgressBar = getView().findViewById(R.id.trainer_rating_4_stars);
        final TextView fourStarTextView = getView().findViewById(R.id.trainer_rating_4_stars_reviewers);
        final ProgressBar fiveStarProgressBar = getView().findViewById(R.id.trainer_rating_5_stars);
        final TextView fiveStarTextView = getView().findViewById(R.id.trainer_rating_5_stars_reviewers);

        final DatabaseReference databaseTrainerReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId + "/username");

        databaseTrainerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trainerNameTextView.setText(dataSnapshot.getValue(String.class));
                databaseTrainerReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseRatingReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId + "/rating");
        mRatingValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRating = dataSnapshot.getValue(Rating.class);

                /*int reviewersNum = mRating.getOne_star_reviewers_num() + mRating.getTwo_stars_reviewers_num() +
                        mRating.getThree_stars_reviewers_num() + mRating.getFour_stars_reviewers_num() + mRating.getFive_stars_reviewers_num();
                float ratingScore;

                if (reviewersNum == 0) {
                    ratingScore = 0;
                }
                else {
                    ratingScore = mRating.getSum_rates() / reviewersNum;
                }*/

                /*trainerRatingBar.setRating(ratingScore);
                trainerRatingScore.setText(String.format("%,.2f", ratingScore) + " (" + reviewersNum + ")");
                oneStarProgressBar.setMax(reviewersNum);
                oneStarProgressBar.setProgress(mRating.getOne_star_reviewers_num());
                oneStarTextView.setText("(" + mRating.getOne_star_reviewers_num() + ")");

                twoStarProgressBar.setMax(reviewersNum);
                twoStarProgressBar.setProgress(mRating.getTwo_stars_reviewers_num());
                twoStarTextView.setText("(" + mRating.getTwo_stars_reviewers_num() + ")");

                threeStarProgressBar.setMax(reviewersNum);
                threeStarProgressBar.setProgress(mRating.getThree_stars_reviewers_num());
                threeStarTextView.setText("(" + mRating.getThree_stars_reviewers_num() + ")");

                fourStarProgressBar.setMax(reviewersNum);
                fourStarProgressBar.setProgress(mRating.getFour_stars_reviewers_num());
                fourStarTextView.setText("(" + mRating.getFour_stars_reviewers_num() + ")");

                fiveStarProgressBar.setMax(reviewersNum);
                fiveStarProgressBar.setProgress(mRating.getFive_stars_reviewers_num());
                fiveStarTextView.setText("(" + mRating.getFive_stars_reviewers_num() + ")");*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseRatingReference.addValueEventListener(mRatingValueEventListener);
    }

    private void buildReviewsRecyclerView() {
        RecyclerView reviewsRecyclerView = getView().findViewById(R.id.trainer_reviews_list);
        reviewsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mTrainerReviewsListAdapter = new TrainerReviewsListAdapter(mTrainerId);

        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsRecyclerView.setAdapter(mTrainerReviewsListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRatingReference.removeEventListener(mRatingValueEventListener);
    }
}
