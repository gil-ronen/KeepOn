package com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.weekly_schedule_view;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.TimeComparator;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.TimeSlot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrainerViewTimeSlotsAdapter extends RecyclerView.Adapter<TrainerViewTimeSlotsAdapter.MyViewHolder>{

    private Fragment mFragment;
    private ArrayList<TimeSlot> mTimeSlots;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;

    private String mDateForApp;
    private String mDateForDB;



    public TrainerViewTimeSlotsAdapter(Fragment fragment, DatabaseReference ref, String dateForApp, String dateForDB) {
        mFragment = fragment;
        mDatabaseReference = ref;
        mTimeSlots = new ArrayList<>();
        mDateForApp = dateForApp;
        mDateForDB = dateForDB;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.child("currentSumPeopleInGroup").getValue(Integer.class)>0)
                {
                    TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                    timeSlot.setTimeSlotId(dataSnapshot.getKey());
                    for (DataSnapshot data : dataSnapshot.child("traineesId").getChildren()) {
                        timeSlot.addTraineeToTrainerTimeSlots(data.child("userId").getValue(String.class));
                    }
                    mTimeSlots.add(timeSlot);
                    notifyDataSetChanged();
                    Collections.sort(mTimeSlots, new TimeComparator());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for(int i=0; i<mTimeSlots.size(); i++)
                {
                    if(mTimeSlots.get(i).getTimeSlotId().equals(dataSnapshot.getKey()))
                    {
                        if(dataSnapshot.child("currentSumPeopleInGroup").getValue(Integer.class)>0)
                        {
                            TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                            timeSlot.setTimeSlotId(dataSnapshot.getKey());
                            for (DataSnapshot data : dataSnapshot.child("traineesId").getChildren()) {
                                timeSlot.addTraineeToTrainerTimeSlots(data.child("userId").getValue(String.class));

                            }
                            mTimeSlots.set(i, timeSlot);
                        }
                        else
                        {
                            mTimeSlots.remove(i);
                        }
                        notifyDataSetChanged();
                        Collections.sort(mTimeSlots, new TimeComparator());
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                for(int i=0; i<mTimeSlots.size(); i++)
                {
                    if(mTimeSlots.get(i).getTimeSlotId().equals(dataSnapshot.getKey()))
                    {
                        if(dataSnapshot.child("currentSumPeopleInGroup").getValue(Integer.class)>0)
                        {
                            mTimeSlots.remove(i);
                            Collections.sort(mTimeSlots, new TimeComparator());
                            notifyDataSetChanged();
                            break;
                        }
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
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trainee_fragment_time_slot,viewGroup, false));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTraineeNameSlot, mTitleSlot, mTimeSlot, mIsFull;
        CircleImageView mImageView;
        LinearLayout mTimeSlotLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTraineeNameSlot = (TextView) itemView.findViewById(R.id.fragment_trainee_name);
            mTitleSlot = (TextView) itemView.findViewById(R.id.fragment_training_title);
            mTimeSlot = (TextView) itemView.findViewById(R.id.fragment_training_time);
            mIsFull = (TextView) itemView.findViewById(R.id.fragment_if_slot_occupied);
            mImageView = (CircleImageView) itemView.findViewById(R.id.fragment_profile_img_trainee_list);
            mTimeSlotLinearLayout = (LinearLayout) itemView.findViewById(R.id.fragment_all_training_slot);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final String getSlotId = mTimeSlots.get(i).getTimeSlotId();

        final String getTimeFrom = mTimeSlots.get(i).getTimeFrom();
        final String getTimeUntil = mTimeSlots.get(i).getTimeUntil();
        final String getTimes = getTimeFrom + " - " + getTimeUntil;
        final int getCurrentSumPeopleInGroup = mTimeSlots.get(i).getCurrentSumPeopleInGroup();
        final int getGroupLimit = mTimeSlots.get(i).getGroupLimit();
        final boolean getGroupSession = mTimeSlots.get(i).isGroupSession();
        final String getTitleSlot = mTimeSlots.get(i).getTitle();
        final String getDescSlot = mTimeSlots.get(i).getDescription();
        final int index = i;

        /*
        final String getTrainerId = mTimeSlots.get(i).getTrainerId();
        //final String getTraineeId = mTimeSlots.get(i).getTraineeId(); //TODO: GET LIST OF TRAINEES
        final boolean isOccupied = mTimeSlots.get(i).isOccupied();
        final boolean getGroupSession = mTimeSlots.get(i).isGroupSession();

        */

        if(getGroupSession)
        {
            myViewHolder.mTraineeNameSlot.setText("Group Training");
            myViewHolder.mImageView.setImageResource(R.drawable.group);
            if(getCurrentSumPeopleInGroup == getGroupLimit)
            {
                myViewHolder.mIsFull.setVisibility(View.VISIBLE);
            }
            else
            {
                myViewHolder.mIsFull.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            if(mTimeSlots.get(i).getSizeOfTraineesList() > 0)
            {
                final String traineeId = mTimeSlots.get(i).getTraineeId(0);
                final DatabaseReference databaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainees").child(traineeId);
                databaseTraineeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String traineeName = dataSnapshot.child("/Profile/name").getValue(String.class);
                        String traineeImageUrl = dataSnapshot.child("/Profile/profilePhotoUrl").getValue(String.class);

                        myViewHolder.mTraineeNameSlot.setText(traineeName);
                        Picasso.with(mFragment.getContext()).load(traineeImageUrl).fit().into(myViewHolder.mImageView);

                        databaseTraineeReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


        }



        myViewHolder.mTitleSlot.setText(mTimeSlots.get(i).getTitle());
        myViewHolder.mTimeSlot.setText(getTimes);
        myViewHolder.mTimeSlotLinearLayout.setBackgroundResource(R.drawable.bg_item_registered_slot);



        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getGroupSession)
                {

                    Intent groupListIntent = new Intent(mFragment.getContext() , TrainingsListGroupParticipatesActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("slotId", getSlotId);
                    bundle.putString("dateForDB", mDateForDB);
                    bundle.putString("trainingDate", mDateForApp);
                    bundle.putString("trainingTitle", getTitleSlot);
                    bundle.putString("trainingDesc", getDescSlot);
                    bundle.putString("trainingTime", getTimes);
                    bundle.putString("sumTrainees", "Total registered "+ getCurrentSumPeopleInGroup +" participants out of " + getGroupLimit);


                    groupListIntent.putExtras(bundle);
                    mFragment.getContext().startActivity(groupListIntent);

                }
                else
                {
                    if(mTimeSlots.get(index).getSizeOfTraineesList() > 0)
                    {
                        final String traineeId = mTimeSlots.get(index).getTraineeId(0);
                        //TODO: OnItemClicked move to profile trainee page
                        Intent traineeProfile = new Intent(mFragment.getContext() , TraineeProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("traineeId", traineeId);
                        traineeProfile.putExtras(bundle);
                        mFragment.getContext().startActivity(traineeProfile);
                    }

                }


            }
        });


        // import font
        Typeface MLight = Typeface.createFromAsset(mFragment.getContext().getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(mFragment.getContext().getAssets(), "fonts/MM.ttf");


        //myViewHolder.mTraineeNameSlot.setText(getTitleSlot);
        myViewHolder.mTraineeNameSlot.setTypeface(MMedium);

        //myViewHolder.mTimeSlot.setText(getTimes);
        myViewHolder.mTimeSlot.setTypeface(MLight);

        //myViewHolder.mTitleSlot.setText(getDescSlot);
        myViewHolder.mTitleSlot.setTypeface(MMedium);
    }

    @Override
    public int getItemCount() {
        return mTimeSlots.size();
    }



    public void clenup()
    {
        mDatabaseReference.removeEventListener(mChildEventListener);
    }

}