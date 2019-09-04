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
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainer_weekly_planner.TimeComparator;
import com.gil_shiran_or.keepon.trainer_weekly_planner.TimeSlot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;


public class TraineeTimeSlotsAdapter extends RecyclerView.Adapter<TraineeTimeSlotsAdapter.MyViewHolder>{

    private Activity mActivity;
    private ArrayList<TimeSlot> mTimeSlots;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;

    private String mDateForApp;
    private String mDateForDB;
    private String mTraineeId;



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

                mTimeSlots.add(timeSlot);
                notifyDataSetChanged();
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
                        mTimeSlots.set(i, timeSlot);
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

        TextView mTitleSlot, mDescSlot, mTimeSlot;
        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleSlot = (TextView) itemView.findViewById(R.id.slot_title);
            mDescSlot = (TextView) itemView.findViewById(R.id.slot_desc);
            mTimeSlot = (TextView) itemView.findViewById(R.id.slot_date_time);
            mImageView = (ImageView) itemView.findViewById(R.id.slot_group_icon);
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
        //final String getDay = mTimeSlots.get(i).getDay();
        final boolean isOccupied = mTimeSlots.get(i).isOccupied();
        final boolean getGroupSession = mTimeSlots.get(i).isGroupSession();

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

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(mActivity , RegisterSlotActivity.class);

                Bundle bundle = new Bundle();



                bundle.putString("title", getTitleSlot);
                bundle.putString("description", getDescSlot);
                bundle.putString("dateForApp", mDateForApp);
                bundle.putString("timeFrom", getTimeFrom);
                bundle.putString("timeUntil", getTimeUntil);
                bundle.putString("dateForDB", mDateForDB);
                bundle.putString("key", getIdSlot);
                bundle.putString("traineeId", mTraineeId);
                bundle.putBoolean("isOccupied", isOccupied);
                bundle.putBoolean("isGroupSession", getGroupSession);


                editIntent.putExtras(bundle);
                mActivity.startActivity(editIntent);
            }
        });


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