package com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.weekly_schedule_view;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.status.TraineeWeeklyTask;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.TimeSlot;

import com.gil_shiran_or.keepon.trainings_weekly_schedule.TraineeRegisterTimeSlot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TraineesListAdapter extends RecyclerView.Adapter<TraineesListAdapter.MyViewHolder> {

    private Activity mActivity;
    //private ArrayList<TimeSlot> mTimeSlots;
    private ArrayList<TraineeRegisterTimeSlot> mTraineesId;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;
    private String mDateForApp;


    public TraineesListAdapter(Activity activity, DatabaseReference ref, String dateForApp) {
        mActivity = activity;
        mDatabaseReference = ref;
        mDateForApp = dateForApp;
        mTraineesId = new ArrayList<>();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TraineeRegisterTimeSlot traineeRegisterTimeSlot = dataSnapshot.getValue(TraineeRegisterTimeSlot.class);
                mTraineesId.add(traineeRegisterTimeSlot);

                //TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                //timeSlot.setTimeSlotId(dataSnapshot.getKey());

                //mTimeSlots.add(timeSlot);
                notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (int i = 0; i < mTraineesId.size(); i++) {
                    if (mTraineesId.get(i).getUserId().equals(dataSnapshot.child("userId").getValue())) {
                        TraineeRegisterTimeSlot traineeRegisterTimeSlot = dataSnapshot.getValue(TraineeRegisterTimeSlot.class);
                        mTraineesId.set(i, traineeRegisterTimeSlot);
                        //TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                        //timeSlot.setTimeSlotId(dataSnapshot.getKey());
                        //mTraineesId.set(i, timeSlot);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                for (int i = 0; i < mTraineesId.size(); i++) {
                    if (mTraineesId.get(i).getUserId().equals(dataSnapshot.child("userId").getValue())) {
                        mTraineesId.remove(i);
                        notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trainee_time_slot, viewGroup, false));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTraineeNameSlot;
        CircleImageView mImageView;
        LinearLayout mTimeSlotLinearLayout;
        LinearLayout mTraineeScoredLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTraineeNameSlot = (TextView) itemView.findViewById(R.id.trainee_name);
            mImageView = (CircleImageView) itemView.findViewById(R.id.profile_img_trainee_list);
            mTimeSlotLinearLayout = (LinearLayout) itemView.findViewById(R.id.all_training_slot);
            mTraineeScoredLinearLayout = (LinearLayout) itemView.findViewById(R.id.is_trainee_got_score);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        /*
        final String getSlotId = mTimeSlots.get(i).getTimeSlotId();
        final String getTitleSlot = mTimeSlots.get(i).getTitle();
        final String getDescSlot = mTimeSlots.get(i).getDescription();
        final String getTimeFrom = mTimeSlots.get(i).getTimeFrom();
        final String getTimeUntil = mTimeSlots.get(i).getTimeUntil();
        final String getTimes = getTimeFrom + " - " + getTimeUntil;
        final String getTrainerId = mTimeSlots.get(i).getTrainerId();
        //final String getTraineeId = mTimeSlots.get(i).getTraineeId(); //TODO: GET LIST OF TRAINEES
        final boolean isOccupied = mTimeSlots.get(i).isOccupied();
        final boolean getGroupSession = mTimeSlots.get(i).isGroupSession();
        final int getCurrentSumPeopleInGroup = mTimeSlots.get(i).getCurrentSumPeopleInGroup();
        final int getGroupLimit = mTimeSlots.get(i).getGroupLimit();
        */

        final String getTraineeId = mTraineesId.get(i).getUserId();
        final boolean isScored = mTraineesId.get(i).getIsGotScore();

        if (isScored) {
            myViewHolder.mTraineeScoredLinearLayout.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.mTraineeScoredLinearLayout.setVisibility(View.INVISIBLE);
        }


        myViewHolder.mTimeSlotLinearLayout.setBackgroundResource(R.drawable.bg_item_registered_slot);


        if (mTraineesId.size() > 0) {
            final DatabaseReference databaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainees").child(getTraineeId);
            databaseTraineeReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String traineeName = dataSnapshot.child("/Profile/name").getValue(String.class);
                    String traineeImageUrl = dataSnapshot.child("/Profile/profilePhotoUrl").getValue(String.class);

                    myViewHolder.mTraineeNameSlot.setText(traineeName);
                    Picasso.with(mActivity).load(traineeImageUrl).fit().into(myViewHolder.mImageView);

                    databaseTraineeReference.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (!mDateForApp.equals("TODAY")) {
                    Toast.makeText(mActivity, "Score cannot be given for future training", Toast.LENGTH_LONG).show();
                } else {


                    mDatabaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("userId").getValue(String.class).equals(getTraineeId)) {
                                    if (dataSnapshot1.child("isGotScore").getValue(boolean.class)) {
                                        Toast.makeText(mActivity, "This trainee already got his score for this training!", Toast.LENGTH_LONG).show();

                                    } else {
                                        mDatabaseReference.child(dataSnapshot1.getKey()).child("isGotScore").setValue(true);
                                        Toast.makeText(mActivity, "Trainee's score updated successfully!", Toast.LENGTH_LONG).show();
                                        myViewHolder.mTraineeScoredLinearLayout.setVisibility(View.VISIBLE);

                                        final DatabaseReference databaseTraineeStatusReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainees").child(getTraineeId).child("Status");
                                        databaseTraineeStatusReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(final @NonNull DataSnapshot dataSnapshotStatus) {
                                                int totalScore = dataSnapshotStatus.child("totalScore").getValue(Integer.class) + 10;

                                                for (DataSnapshot data : dataSnapshotStatus.child("weeklyTasks").getChildren()) {
                                                    TraineeWeeklyTask traineeWeeklyTask = data.getValue(TraineeWeeklyTask.class);

                                                    if (traineeWeeklyTask.getIsCompleted()) {
                                                        continue;
                                                    }

                                                    int times = traineeWeeklyTask.getTimes() + 1;
                                                    databaseTraineeStatusReference.child("weeklyTasks").child(data.getKey()).child("times").setValue(times);

                                                    if (times == traineeWeeklyTask.getTotalTimes()) {
                                                        databaseTraineeStatusReference.child("weeklyTasks").child(data.getKey()).child("isCompleted").setValue(true);
                                                        totalScore += traineeWeeklyTask.getScore();
                                                    }
                                                }

                                                final int totalScorreToChange = totalScore;
                                                if (totalScore >= dataSnapshotStatus.child("scoreToNextLevel").getValue(Integer.class)) {
                                                    int level = dataSnapshotStatus.child("level").getValue(Integer.class) + 1;
                                                    databaseTraineeStatusReference.child("level").setValue(level);
                                                    String levelId = "Level" + level;
                                                    final DatabaseReference databaseLevelsReference = FirebaseDatabase.getInstance().getReference().child("Levels").child(levelId);
                                                    databaseLevelsReference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            int scoreToNextLevel = dataSnapshot.child("scoreToNextLevel").getValue(Integer.class);
                                                            databaseTraineeStatusReference.child("scoreToNextLevel").setValue(scoreToNextLevel);
                                                            databaseTraineeStatusReference.child("totalScore").setValue(totalScorreToChange);
                                                            databaseLevelsReference.removeEventListener(this);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }

                                                databaseTraineeStatusReference.removeEventListener(this);

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    break;
                                }
                            }

                            mDatabaseReference.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                return true;
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent traineeProfile = new Intent(mActivity, TraineeProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("traineeId", getTraineeId);
                traineeProfile.putExtras(bundle);
                mActivity.startActivity(traineeProfile);

            }
        });


        // import font
        Typeface MMedium = Typeface.createFromAsset(mActivity.getAssets(), "fonts/MM.ttf");


        myViewHolder.mTraineeNameSlot.setTypeface(MMedium);

    }

    @Override
    public int getItemCount() {
        return mTraineesId.size();
    }


    public void clenup() {
        mDatabaseReference.removeEventListener(mChildEventListener);
    }

}