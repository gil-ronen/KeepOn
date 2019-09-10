package com.gil_shiran_or.keepon.trainee.search_friend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.status.TraineeWeeklyTask;
import com.gil_shiran_or.keepon.trainee.status.WeeklyTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TraineeWeeklyTasksListAdapter extends RecyclerView.Adapter<TraineeWeeklyTasksListAdapter.WeeklyTasksListViewHolder> {

    private List<TraineeWeeklyTask> mTraineeWeeklyTasksList = new ArrayList<>();
    private DatabaseReference mDatabaseTraineeWeeklyTasksReference;
    ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseWeeklyTasksReference = FirebaseDatabase.getInstance().getReference().child("WeeklyTasks");

    public static class WeeklyTasksListViewHolder extends RecyclerView.ViewHolder {

        public TextView taskDescriptionTextView;
        public TextView taskScoreTextView;
        public ImageView taskCompletedImageView;

        public WeeklyTasksListViewHolder(View itemView) {
            super(itemView);
            taskDescriptionTextView = itemView.findViewById(R.id.weekly_task_description);
            taskScoreTextView = itemView.findViewById(R.id.weekly_task_score);
            taskCompletedImageView = itemView.findViewById(R.id.weekly_task_completed);
        }
    }

    public TraineeWeeklyTasksListAdapter(String traineeId) {
        mDatabaseTraineeWeeklyTasksReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + traineeId + "/Status/weeklyTasks");

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mTraineeWeeklyTasksList.add(dataSnapshot.getValue(TraineeWeeklyTask.class));
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mTraineeWeeklyTasksList.size(); i++) {
                    if (mTraineeWeeklyTasksList.get(i).getTaskId().equals(dataSnapshot.child("taskId").getValue(String.class))) {
                        mTraineeWeeklyTasksList.set(i, dataSnapshot.getValue(TraineeWeeklyTask.class));
                        break;
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                mTraineeWeeklyTasksList = new ArrayList<>();
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeWeeklyTasksReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public WeeklyTasksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_task_item, parent, false);

        return new WeeklyTasksListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WeeklyTasksListViewHolder holder, int position) {
        final TraineeWeeklyTask currentTraineeWeeklyTask = mTraineeWeeklyTasksList.get(position);

        mDatabaseWeeklyTasksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.taskDescriptionTextView.setText(dataSnapshot.child(currentTraineeWeeklyTask.getTaskId() + "/description").getValue(String.class));
                holder.taskScoreTextView.setText(Integer.toString(dataSnapshot.child(currentTraineeWeeklyTask.getTaskId() + "/score").getValue(Integer.class)));

                mDatabaseWeeklyTasksReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (currentTraineeWeeklyTask.getIsCompleted()) {
            holder.taskCompletedImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mTraineeWeeklyTasksList.size();
    }

    public void cleanUp() {
        mDatabaseTraineeWeeklyTasksReference.removeEventListener(mChildEventListener);
    }
}
