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

import com.gil_shiran_or.keepon.R;
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


    public TraineesListAdapter(Activity activity, DatabaseReference ref) {
        mActivity = activity;
        mDatabaseReference = ref;
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTraineeNameSlot = (TextView) itemView.findViewById(R.id.trainee_name);
            mImageView = (CircleImageView) itemView.findViewById(R.id.profile_img_trainee_list);
            mTimeSlotLinearLayout = (LinearLayout) itemView.findViewById(R.id.all_training_slot);
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



        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent traineeProfileIntent = new Intent(mActivity, null. class);

                Bundle bundle = new Bundle();

                //bundle.putString("traineeId", getTraineeId);


                traineeProfileIntent.putExtras(bundle);
                mActivity.startActivity(traineeProfileIntent);
                */
            }
        });


        // import font
        //Typeface MLight = Typeface.createFromAsset(mActivity.getAssets(), "fonts/ML.ttf");
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