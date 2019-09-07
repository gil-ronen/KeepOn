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
import com.gil_shiran_or.keepon.trainings_weekly_schedule.TimeComparator;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.TimeSlot;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.create_and_edit_time_slots.EditSlotActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;

public class TrainerViewTimeSlotsAdapter extends RecyclerView.Adapter<TrainerViewTimeSlotsAdapter.MyViewHolder>{

    private Activity mActivity;
    private ArrayList<TimeSlot> mTimeSlots;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;

    private String mDateForApp;
    private String mDateForDB;



    public TrainerViewTimeSlotsAdapter(Activity activity, DatabaseReference ref, String dateForApp, String dateForDB) {
        mActivity = activity;
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
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_time_slot,viewGroup, false));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTraineeNameSlot, mTitleSlot, mTimeSlot;
        ImageView mImageView;
        LinearLayout mTimeSlotLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTraineeNameSlot = (TextView) itemView.findViewById(R.id.slot_title);
            mTitleSlot = (TextView) itemView.findViewById(R.id.slot_desc);
            mTimeSlot = (TextView) itemView.findViewById(R.id.slot_date_time);
            mImageView = (ImageView) itemView.findViewById(R.id.slot_group_icon);
            mTimeSlotLinearLayout = (LinearLayout) itemView.findViewById(R.id.all_time_slot);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final String getIdSlot = mTimeSlots.get(i).getTimeSlotId();
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


        myViewHolder.mTitleSlot.setText(mTimeSlots.get(i).getTitle());
        myViewHolder.mTimeSlot.setText(getTimes);
        myViewHolder.mTimeSlotLinearLayout.setBackgroundResource(R.drawable.bg_item_registered_slot);

        if(getGroupSession)
        {
            myViewHolder.mTraineeNameSlot.setText("Group Training");
            myViewHolder.mImageView.setImageResource(R.drawable.group);
        }
        else
        {
            myViewHolder.mTraineeNameSlot.setText("Trainee Name"); //TODO: GET TRAINEE IMAGE
            myViewHolder.mImageView.setImageResource(R.drawable.one_person); //TODO: GET TRAINEE IMAGE
        }


        //TODO: OnItemClicked move to profile trainee page

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getGroupSession)
                {
                    /*
                    Intent editIntent = new Intent(mActivity , EditSlotActivity.class);

                    Bundle bundle = new Bundle();


                    bundle.putString("title", getTitleSlot);
                    bundle.putString("description", getDescSlot);
                    bundle.putString("dateForApp", mDateForApp);
                    bundle.putString("timeFrom", getTimeFrom);
                    bundle.putString("timeUntil", getTimeUntil);
                    bundle.putString("dateForDB", mDateForDB);
                    bundle.putString("key", getIdSlot);
                    //bundle.putString("currentTraineeId", mTraineeId);
                    //bundle.putString("traineeId", getTraineeId);
                    bundle.putString("trainerId", getTrainerId);
                    bundle.putBoolean("isOccupied", isOccupied);
                    bundle.putBoolean("isGroupSession", getGroupSession);
                    bundle.putInt("currentSumPeopleInGroup", getCurrentSumPeopleInGroup);
                    bundle.putInt("groupLimit", getGroupLimit);

                    editIntent.putExtras(bundle);
                    mActivity.startActivity(editIntent);
                    */
                }
                else
                {
                    //TODO: OnItemClicked move to profile trainee page
                }


            }
        });


        // import font
        Typeface MLight = Typeface.createFromAsset(mActivity.getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(mActivity.getAssets(), "fonts/MM.ttf");


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