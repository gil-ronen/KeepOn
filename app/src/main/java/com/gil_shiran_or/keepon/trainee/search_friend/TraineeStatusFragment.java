package com.gil_shiran_or.keepon.trainee.search_friend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.db.Rating;
import com.gil_shiran_or.keepon.db.Status;
import com.gil_shiran_or.keepon.trainee.search_trainer.TrainerReviewsListAdapter;
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TraineeStatusFragment extends Fragment {

    private DatabaseReference mDatabaseStatusReference;
    private TraineeWeeklyTasksListAdapter mTraineeWeeklyTasksListAdapter;
    private ValueEventListener mStatusValueEventListener;
    private String mTraineeId;
    private Status mStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_friend_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTraineeId = getArguments().getString("traineeId");

        buildReviewsRecyclerView();

        final TextView traineeLevelTextView = getView().findViewById(R.id.trainee_level);
        final BootstrapProgressBar traineeProgressBar = getView().findViewById(R.id.trainee_progress_bar);
        final TextView traineeTotalScoreTextView = getView().findViewById(R.id.trainee_total_score);
        final TextView traineeScoreToNextLevelTextView = getView().findViewById(R.id.trainee_score_to_next_level);

        mDatabaseStatusReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/Status");
        mStatusValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mStatus = dataSnapshot.getValue(Status.class);

                traineeLevelTextView.setText("Level" + mStatus.getLevel());
                traineeProgressBar.setMaxProgress(mStatus.getTotalScore() + mStatus.getScoreToNextLevel());
                traineeProgressBar.setProgress(mStatus.getTotalScore());
                traineeTotalScoreTextView.setText(Integer.toString(mStatus.getTotalScore()));
                traineeScoreToNextLevelTextView.setText(Integer.toString(mStatus.getScoreToNextLevel()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseStatusReference.addValueEventListener(mStatusValueEventListener);
    }

    private void buildReviewsRecyclerView() {
        RecyclerView weeklyTasksRecyclerView = getView().findViewById(R.id.trainee_weekly_tasks_list);
        weeklyTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mTraineeWeeklyTasksListAdapter = new TraineeWeeklyTasksListAdapter(mTraineeId);

        weeklyTasksRecyclerView.setLayoutManager(layoutManager);
        weeklyTasksRecyclerView.setAdapter(mTraineeWeeklyTasksListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseStatusReference.removeEventListener(mStatusValueEventListener);
        mTraineeWeeklyTasksListAdapter.cleanUp();
    }
}
