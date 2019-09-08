package com.gil_shiran_or.keepon.trainee.my_trainers;

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
import com.gil_shiran_or.keepon.db.Rating;
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MyTrainerRatingFragment extends Fragment implements AddReviewDialog.AddReviewListener {

    private DatabaseReference mDatabaseRatingReference;
    private DatabaseReference mDatabaseMyTrainersReference;
    private MyTrainerReviewsListAdapter mMyTrainerReviewsListAdapter;
    private ValueEventListener mRatingValueEventListener;
    private ValueEventListener mReviewValueEventListener;
    private String mTrainerId;
    private String mMyTrainerKey;
    private MyTrainer mMyTrainer;
    private String mCurrentUserId;
    private Rating mRating;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainer_rating, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTrainerId = getArguments().getString("trainerId");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        final FloatingActionButton addReviewFloatingActionButton = getView().findViewById(R.id.my_trainer_rating_add_review_button);
        final MyTrainerRatingFragment fragment = this;

        mDatabaseMyTrainersReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyTrainers");
        mReviewValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    MyTrainer myTrainer = data.getValue(MyTrainer.class);
                    if (myTrainer.getUserId().equals(mTrainerId)) {
                        if (!myTrainer.getIsRated()) {
                            mMyTrainer = myTrainer;
                            mMyTrainerKey = data.getKey();

                            addReviewFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AddReviewDialog addReviewDialog = new AddReviewDialog();

                                    addReviewDialog.setTargetFragment(fragment, 0);
                                    addReviewDialog.show(getFragmentManager(), "add review dialog");
                                }
                            });
                        }
                        else {
                            addReviewFloatingActionButton.setVisibility(View.GONE);
                            mDatabaseMyTrainersReference.removeEventListener(this);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseMyTrainersReference.addValueEventListener(mReviewValueEventListener);

        buildReviewsRecyclerView();

        ViewGroup reviewsExpanderViewGroup = getView().findViewById(R.id.my_trainer_reviews_expander);
        ViewGroup reviewsViewGroup = getView().findViewById(R.id.my_trainer_reviews_list);

        new ExpandableViewGroup("View reviews", "Hide reviews", reviewsExpanderViewGroup, reviewsViewGroup);

        final TextView trainerNameTextView = getView().findViewById(R.id.my_trainer_rating_name);
        final RatingBar trainerRatingBar = getView().findViewById(R.id.my_trainer_rating);
        final TextView trainerRatingScore = getView().findViewById(R.id.my_trainer_rating_score);
        final ProgressBar oneStarProgressBar = getView().findViewById(R.id.my_trainer_rating_1_star);
        final TextView oneStarTextView = getView().findViewById(R.id.my_trainer_rating_1_star_reviewers);
        final ProgressBar twoStarProgressBar = getView().findViewById(R.id.my_trainer_rating_2_stars);
        final TextView twoStarTextView = getView().findViewById(R.id.my_trainer_rating_2_stars_reviewers);
        final ProgressBar threeStarProgressBar = getView().findViewById(R.id.my_trainer_rating_3_stars);
        final TextView threeStarTextView = getView().findViewById(R.id.my_trainer_rating_3_stars_reviewers);
        final ProgressBar fourStarProgressBar = getView().findViewById(R.id.my_trainer_rating_4_stars);
        final TextView fourStarTextView = getView().findViewById(R.id.my_trainer_rating_4_stars_reviewers);
        final ProgressBar fiveStarProgressBar = getView().findViewById(R.id.my_trainer_rating_5_stars);
        final TextView fiveStarTextView = getView().findViewById(R.id.my_trainer_rating_5_stars_reviewers);

        final DatabaseReference databaseTrainerReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId + "/Profile/name");

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

        mDatabaseRatingReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId + "/Rating");
        mRatingValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRating = dataSnapshot.getValue(Rating.class);

                trainerRatingBar.setRating(mRating.getRating());
                trainerRatingScore.setText(String.format("%,.2f", mRating.getRating()) + " (" + mRating.getTotalRaters() + ")");
                oneStarProgressBar.setMax(mRating.getTotalRaters());
                oneStarProgressBar.setProgress(mRating.getOneStarRaters());
                oneStarTextView.setText("(" + mRating.getOneStarRaters() + ")");

                twoStarProgressBar.setMax(mRating.getTotalRaters());
                twoStarProgressBar.setProgress(mRating.getTwoStarsRaters());
                twoStarTextView.setText("(" + mRating.getTwoStarsRaters() + ")");

                threeStarProgressBar.setMax(mRating.getTotalRaters());
                threeStarProgressBar.setProgress(mRating.getThreeStarsRaters());
                threeStarTextView.setText("(" + mRating.getThreeStarsRaters() + ")");

                fourStarProgressBar.setMax(mRating.getTotalRaters());
                fourStarProgressBar.setProgress(mRating.getFourStarsRaters());
                fourStarTextView.setText("(" + mRating.getFourStarsRaters() + ")");

                fiveStarProgressBar.setMax(mRating.getTotalRaters());
                fiveStarProgressBar.setProgress(mRating.getFiveStarsRaters());
                fiveStarTextView.setText("(" + mRating.getFiveStarsRaters() + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseRatingReference.addValueEventListener(mRatingValueEventListener);
    }

    @Override
    public void applyReview(float rating, String review) {
        if (!review.isEmpty()) {
            mMyTrainerReviewsListAdapter.setReviewToFirebase(new Review(rating, review));
        }

        Map<String, Object> childUpdates = new HashMap<>();
        mMyTrainer.setIsRated(true);

        childUpdates.put(mMyTrainerKey, mMyTrainer);
        mDatabaseMyTrainersReference.updateChildren(childUpdates);
        changeRatingInFirebase(rating);
    }

    private void buildReviewsRecyclerView() {
        RecyclerView reviewsRecyclerView = getView().findViewById(R.id.my_trainer_reviews_list);
        reviewsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMyTrainerReviewsListAdapter = new MyTrainerReviewsListAdapter(mTrainerId);

        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsRecyclerView.setAdapter(mMyTrainerReviewsListAdapter);
    }

    private void changeRatingInFirebase(float rating) {
        Map<String, Object> childUpdates = new HashMap<>();

        mRating.setSumRatings((int) rating + mRating.getSumRatings());
        mRating.setTotalRaters(mRating.getTotalRaters() + 1);
        mRating.setRating((float) (mRating.getSumRatings() / mRating.getTotalRaters()));

        if (rating == 1) {
            mRating.setOneStarRaters(mRating.getOneStarRaters() + 1);
        }
        else if (rating == 2) {
            mRating.setTwoStarsRaters(mRating.getTwoStarsRaters() + 1);
        }
        else if (rating == 3) {
            mRating.setThreeStarsRaters(mRating.getThreeStarsRaters() + 1);
        }
        else if (rating == 4) {
            mRating.setFourStarsRaters(mRating.getFourStarsRaters() + 1);
        }
        else {
            mRating.setFiveStarsRaters(mRating.getFiveStarsRaters() + 1);
        }

        childUpdates.put("rating", mRating.getRating());
        childUpdates.put("totalRaters", mRating.getTotalRaters());
        childUpdates.put("sumRatings", mRating.getSumRatings());
        childUpdates.put("oneStarRaters", mRating.getOneStarRaters());
        childUpdates.put("twoStarsRaters", mRating.getTwoStarsRaters());
        childUpdates.put("threeStarsRaters", mRating.getThreeStarsRaters());
        childUpdates.put("fourStarsRaters", mRating.getFourStarsRaters());
        childUpdates.put("fiveStarsRaters", mRating.getFiveStarsRaters());

        mDatabaseRatingReference.updateChildren(childUpdates);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRatingReference.removeEventListener(mRatingValueEventListener);
        mDatabaseMyTrainersReference.removeEventListener(mReviewValueEventListener);
        mMyTrainerReviewsListAdapter.cleanUp();
    }
}
