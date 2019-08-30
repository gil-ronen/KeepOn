package com.gil_shiran_or.keepon.trainer_weekly_planner;


import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TimeSlotsAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private ArrayList<DataSnapshot> mSnapshotList;

    private FirebaseAuth mAuth;
    //private DatabaseReference mDatabaseUsersReference;



    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            //TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);

            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            for(int i=0; i<mSnapshotList.size(); i++)
            {
                if(mSnapshotList.get(i).getKey().equals(dataSnapshot.getKey()))
                {
                    mSnapshotList.set(i, dataSnapshot);
                    notifyDataSetChanged();
                    break;
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            for(int i=0; i<mSnapshotList.size(); i++)
            {
                if(mSnapshotList.get(i).getKey().equals(dataSnapshot.getKey()))
                {
                    mSnapshotList.remove(i);
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

    public TimeSlotsAdapter(Activity activity, DatabaseReference ref)
    {
        mActivity = activity;
        mDatabaseReference = ref.child("WeeklySchedule");
        mDatabaseReference.addChildEventListener(mListener);

        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder
    {
        TextView title;
        TextView dateAndTime;
        TextView description;
        //LinearLayout.LayoutParams params;
    }


    @Override
    public int getCount() {

        return mSnapshotList.size();
    }

    @Override
    public TimeSlot getItem(int position) {

        DataSnapshot snapshot = mSnapshotList.get(position);
        TimeSlot timeSlot = snapshot.getValue(TimeSlot.class);
        timeSlot.setTimeSlotId(snapshot.getKey());
        return timeSlot;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_time_slot, parent, false);

            final TimeSlotsAdapter.ViewHolder holder = new TimeSlotsAdapter.ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.slot_title);
            holder.dateAndTime = (TextView) convertView.findViewById(R.id.slot_date_time);
            holder.description = (TextView) convertView.findViewById(R.id.slot_desc);
            //holder.params = (LinearLayout.LayoutParams) holder.title.getLayoutParams();


            convertView.setTag(holder);
        }

        final  TimeSlot timeSlot = getItem(position);
        final TimeSlotsAdapter.ViewHolder holder = (TimeSlotsAdapter.ViewHolder) convertView.getTag();

        //setChatRowApperance(isMe, holder);

        String sTitle = timeSlot.getTitle();
        holder.title.setText(sTitle);

        String sDateAndTime = timeSlot.getDateAndTime();
        holder.dateAndTime.setText(sDateAndTime);

        String sDescription = timeSlot.getDescription();
        holder.description.setText(sDescription);


        return convertView;
    }

    /*
    private void setChatRowApperance(boolean isItMe, TimeSlotsAdapter.ViewHolder holder)
    {
        if(isItMe)
        {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }
        else
        {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
        holder.time.setLayoutParams(holder.params);
    }*/

    public void clenup()
    {
        mDatabaseReference.removeEventListener(mListener);
    }

}