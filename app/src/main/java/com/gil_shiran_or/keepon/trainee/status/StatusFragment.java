package com.gil_shiran_or.keepon.trainee.status;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.gil_shiran_or.keepon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatusFragment extends Fragment {

    TraineeWeeklyTasksListAdapter mTraineeWeeklyTasksListAdapter;
    DatabaseReference mDatabaseTraineeReference;
    ValueEventListener mValueEventListener;
    String mCurrentUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Status");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId);

        buildTraineeWeeklyTasksRecyclerView();

        final TextView statusLevelTextView = view.findViewById(R.id.status_level);
        final BootstrapProgressBar statusProgressBar = view.findViewById(R.id.status_progress_bar);
        final TextView statusTotalScoreTextView = view.findViewById(R.id.status_total_score);
        final TextView statusScoreToNextLevelTextView = view.findViewById(R.id.status_score_to_next_level);

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String levelId = "Level " + dataSnapshot.child("level").getValue(Integer.class);
                int totalScore = dataSnapshot.child("total_score").getValue(Integer.class);
                int scoreToNextLevel = dataSnapshot.child("score_to_next_level").getValue(Integer.class);

                statusLevelTextView.setText(levelId);
                statusProgressBar.setMaxProgress(totalScore + scoreToNextLevel);
                statusProgressBar.setProgress(totalScore);
                statusTotalScoreTextView.setText(Integer.toString(totalScore));
                statusScoreToNextLevelTextView.setText(Integer.toString(scoreToNextLevel));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeReference.addValueEventListener(mValueEventListener);
    }

    private void buildTraineeWeeklyTasksRecyclerView() {
        RecyclerView traineeWeeklyTasksRecyclerView = getView().findViewById(R.id.weekly_tasks_list);
        traineeWeeklyTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mTraineeWeeklyTasksListAdapter = new TraineeWeeklyTasksListAdapter();

        traineeWeeklyTasksRecyclerView.setLayoutManager(layoutManager);
        traineeWeeklyTasksRecyclerView.setAdapter(mTraineeWeeklyTasksListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTraineeReference.removeEventListener(mValueEventListener);
        mTraineeWeeklyTasksListAdapter.cleanUp();
    }
}


