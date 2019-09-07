package com.gil_shiran_or.keepon.trainer_weekly_planner.trainee_side;

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
import com.gil_shiran_or.keepon.Status;
import com.gil_shiran_or.keepon.trainer_weekly_planner.TimeComparator;
import com.gil_shiran_or.keepon.trainer_weekly_planner.TimeSlot;
import com.gil_shiran_or.keepon.trainer_weekly_planner.TraineeRegisterTimeSlot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TraineeTimeSlotsAdapter extends RecyclerView.Adapter<TraineeTimeSlotsAdapter.MyViewHolder>{

    private Activity mActivity;
    private ArrayList<TimeSlot> mTimeSlots;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;

    private String mDateForApp;
    private String mDateForDB;
    private String mTraineeId;

    //private boolean isTraineeExistInThisSlot;



    public TraineeTimeSlotsAdapter(Activity activity, DatabaseReference ref, String dateForApp, String dateForDB) {
        mActivity = activity;
        mDatabaseReference = ref;
        mTimeSlots = new ArrayList<>();
        mDateForApp = dateForApp;
        mDateForDB = dateForDB;
        //TODO: TO GET CURRENT TRAINEE ID FROM DB
        mTraineeId = "test1234";

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                timeSlot.setTimeSlotId(dataSnapshot.getKey());
                for (DataSnapshot data : dataSnapshot.child("traineesId").getChildren()) {
                    timeSlot.addTraineeToTrainerTimeSlots(data.child("userId").getValue(String.class));
                }

                mTimeSlots.add(timeSlot);
                notifyDataSetChanged();
                //notifyItemInserted(mTimeSlots.size()-1);
                Collections.sort(mTimeSlots, new TimeComparator());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for(int i=0; i<mTimeSlots.size(); i++)
                {
                    if(mTimeSlots.get(i).getTimeSlotId().equals(dataSnapshot.getKey()))
                    {
                        TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                        timeSlot.setTimeSlotId(dataSnapshot.getKey());
                        //timeSlot.clearTraineesId();
                        for (DataSnapshot data : dataSnapshot.child("traineesId").getChildren()) {
                            timeSlot.addTraineeToTrainerTimeSlots(data.child("userId").getValue(String.class));

                        }

                        mTimeSlots.set(i, timeSlot);
                        notifyItemChanged(i);
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
                        mTimeSlots.remove(i);
                        Collections.sort(mTimeSlots, new TimeComparator());
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
    public TraineeTimeSlotsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TraineeTimeSlotsAdapter.MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_time_slot,viewGroup, false));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleSlot, mDescSlot, mTimeSlot, mIsMe;
        ImageView mImageView;
        LinearLayout mTimeSlotLinearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleSlot = (TextView) itemView.findViewById(R.id.slot_title);
            mDescSlot = (TextView) itemView.findViewById(R.id.slot_desc);
            mTimeSlot = (TextView) itemView.findViewById(R.id.slot_date_time);
            mIsMe = (TextView) itemView.findViewById(R.id.if_slot_occupied_by_user);
            mImageView = (ImageView) itemView.findViewById(R.id.slot_group_icon);
            mTimeSlotLinearLayout = (LinearLayout) itemView.findViewById(R.id.all_time_slot);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final TraineeTimeSlotsAdapter.MyViewHolder myViewHolder, int i) {

        final String getIdSlot = mTimeSlots.get(i).getTimeSlotId();
        final String getTitleSlot = mTimeSlots.get(i).getTitle();
        final String getDescSlot = mTimeSlots.get(i).getDescription();
        final String getTimeFrom = mTimeSlots.get(i).getTimeFrom();
        final String getTimeUntil = mTimeSlots.get(i).getTimeUntil();
        final String getTimes = getTimeFrom + " - " + getTimeUntil;
        final String getTrainerId = mTimeSlots.get(i).getTrainerId();
        final TimeSlot timeSlot = mTimeSlots.get(i); //TODO: GET LIST OF TRAINEES
        final boolean isOccupied = mTimeSlots.get(i).isOccupied();
        final boolean getGroupSession = mTimeSlots.get(i).isGroupSession();
        final int getCurrentSumPeopleInGroup = mTimeSlots.get(i).getCurrentSumPeopleInGroup();
        final int getGroupLimit = mTimeSlots.get(i).getGroupLimit();
        boolean deleteSlotPermission = false;
        //boolean isTraineeExistInThisSlot = false;

        myViewHolder.mTitleSlot.setText(mTimeSlots.get(i).getTitle());
        myViewHolder.mDescSlot.setText(mTimeSlots.get(i).getDescription());
        myViewHolder.mTimeSlot.setText(getTimes);

        if(getGroupSession)
        {
            myViewHolder.mImageView.setImageResource(R.drawable.group);
        }
        else
        {
            myViewHolder.mImageView.setImageResource(R.drawable.one_person);
        }




        if(timeSlot.isTraineesIdListContainsTraineeId(mTraineeId))
        {
            myViewHolder.mIsMe.setVisibility(View.VISIBLE);
            deleteSlotPermission = true;
            //isTraineeExistInThisSlot = true;
        }



        if(isOccupied)
        {
            myViewHolder.mTimeSlotLinearLayout.setBackgroundResource(R.drawable.bg_item_unavailable_slot);
        }
        else
        {
            myViewHolder.mTimeSlotLinearLayout.setBackgroundResource(R.drawable.bg_item_time_available);
            myViewHolder.mIsMe.setVisibility(View.INVISIBLE);
        }




        if(!isOccupied || deleteSlotPermission)
        {

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editIntent = new Intent(mActivity, RegisterSlotActivity.class);

                    Bundle bundle = new Bundle();

                    bundle.putString("title", getTitleSlot);
                    bundle.putString("description", getDescSlot);
                    bundle.putString("dateForApp", mDateForApp);
                    bundle.putString("timeFrom", getTimeFrom);
                    bundle.putString("timeUntil", getTimeUntil);
                    bundle.putString("dateForDB", mDateForDB);
                    bundle.putString("key", getIdSlot);
                    bundle.putString("currentTraineeId", mTraineeId);
                    //bundle.putString("traineeId", getTraineeId);
                    bundle.putString("trainerId", getTrainerId);
                    bundle.putBoolean("isOccupied", isOccupied);
                    bundle.putBoolean("isGroupSession", getGroupSession);
                    bundle.putInt("currentSumPeopleInGroup", getCurrentSumPeopleInGroup);
                    bundle.putInt("groupLimit", getGroupLimit);
                    //bundle.putBoolean("isTraineeExistInThisSlot", isTraineeExistInThisSlot);

                    if(timeSlot.isTraineesIdListContainsTraineeId(mTraineeId))
                    {
                        bundle.putBoolean("isTraineeExistInThisSlot", true);
                    }
                    else
                    {
                        bundle.putBoolean("isTraineeExistInThisSlot", false);
                    }
                    editIntent.putExtras(bundle);

                    mActivity.startActivity(editIntent);

                }
            });
        }
        else
        {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "This training is not available", Toast.LENGTH_SHORT).show();
                }
            });
            //myViewHolder.mTimeSlotLinearLayout.setBackgroundResource(R.drawable.bg_item_unavailable_slot);
        }




        // import font
        Typeface MLight = Typeface.createFromAsset(mActivity.getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(mActivity.getAssets(), "fonts/MM.ttf");


        myViewHolder.mTitleSlot.setText(getTitleSlot);
        myViewHolder.mTitleSlot.setTypeface(MMedium);

        myViewHolder.mTimeSlot.setText(getTimes);
        myViewHolder.mTimeSlot.setTypeface(MLight);

        myViewHolder.mDescSlot.setText(getDescSlot);
        myViewHolder.mDescSlot.setTypeface(MMedium);
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