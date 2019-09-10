package com.gil_shiran_or.keepon.trainee.my_friends;

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
import com.gil_shiran_or.keepon.db.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyFriendStatusFragment extends Fragment {

    private DatabaseReference mDatabaseStatusReference;
    private MyFriendWeeklyTasksListAdapter mMyFriendWeeklyTasksListAdapter;
    private ValueEventListener mStatusValueEventListener;
    private String mTraineeId;
    private Status mStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_friend_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTraineeId = getArguments().getString("traineeId");

        buildReviewsRecyclerView();

        final TextView traineeLevelTextView = getView().findViewById(R.id.my_friend_level);
        final BootstrapProgressBar traineeProgressBar = getView().findViewById(R.id.my_friend_progress_bar);
        final TextView traineeTotalScoreTextView = getView().findViewById(R.id.my_friend_total_score);
        final TextView traineeScoreToNextLevelTextView = getView().findViewById(R.id.my_friend_score_to_next_level);

        mDatabaseStatusReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/Status");
        mStatusValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mStatus = dataSnapshot.getValue(Status.class);

                traineeLevelTextView.setText(mStatus.getLevel());
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
        RecyclerView weeklyTasksRecyclerView = getView().findViewById(R.id.my_friend_weekly_tasks_list);
        weeklyTasksRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMyFriendWeeklyTasksListAdapter = new MyFriendWeeklyTasksListAdapter(mTraineeId);

        weeklyTasksRecyclerView.setLayoutManager(layoutManager);
        weeklyTasksRecyclerView.setAdapter(mMyFriendWeeklyTasksListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseStatusReference.removeEventListener(mStatusValueEventListener);
        mMyFriendWeeklyTasksListAdapter.cleanUp();
    }
}
