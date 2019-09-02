package com.gil_shiran_or.keepon.trainer_weekly_planner;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.MyViewHolder>{

    private Activity mActivity;
    private ArrayList<TimeSlot> mTimeSlots;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseReference;
    //private FirebaseAuth mAuth;


    public TimeSlotsAdapter(Activity activity, DatabaseReference ref) {
        mActivity = activity;
        mDatabaseReference = ref.child("WeeklySchedule");
        mTimeSlots = new ArrayList<>();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                timeSlot.setTimeSlotId(dataSnapshot.getKey());

                mTimeSlots.add(timeSlot);
                notifyDataSetChanged();
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
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_time_slot,viewGroup, false));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleSlot, mDescSlot, mTimeSlot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleSlot = (TextView) itemView.findViewById(R.id.slot_title);
            mDescSlot = (TextView) itemView.findViewById(R.id.slot_desc);
            mTimeSlot = (TextView) itemView.findViewById(R.id.slot_date_time);
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
        final String getDay = mTimeSlots.get(i).getDay();
        final boolean isOccupied = mTimeSlots.get(i).isOccupied();
        final boolean getGroupSession = mTimeSlots.get(i).isGroupSession();

        myViewHolder.mTitleSlot.setText(mTimeSlots.get(i).getTitle());
        myViewHolder.mDescSlot.setText(mTimeSlots.get(i).getDescription());
        myViewHolder.mTimeSlot.setText(getTimes);


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(mActivity ,EditSlotActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("key", getIdSlot);
                bundle.putString("title", getTitleSlot);
                bundle.putString("description", getDescSlot);
                bundle.putString("timeFrom", getTimeFrom);
                bundle.putString("timeUntil", getTimeUntil);
                bundle.putString("day", getDay);
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