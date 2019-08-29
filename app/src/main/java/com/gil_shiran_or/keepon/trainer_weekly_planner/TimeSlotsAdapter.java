package com.gil_shiran_or.keepon.trainer_weekly_planner;


import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.util.ArrayList;

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.MyViewHolder>{

    Context mContext;
    ArrayList<TimeSlot> mTimeSlots;

    public TimeSlotsAdapter(Context context, ArrayList<TimeSlot> slots) {
        mContext = context;
        mTimeSlots = slots;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_time_slot,viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.mTitleSlot.setText(mTimeSlots.get(i).getTitle());
        myViewHolder.mDescSlot.setText(mTimeSlots.get(i).getDescription());
        myViewHolder.mDateTimeSlot.setText(mTimeSlots.get(i).getDateAndTime());

        final String getTitleSlot = mTimeSlots.get(i).getTitle();
        final String getDescSlot = mTimeSlots.get(i).getDescription();
        final String getDateSlot = mTimeSlots.get(i).getDateAndTime();
        final String getKeySlot = mTimeSlots.get(i).getKey();


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,EditSlotActivity.class);
                intent.putExtra("title", getTitleSlot);
                intent.putExtra("description", getDescSlot);
                intent.putExtra("dateAndTime", getDateSlot);
                intent.putExtra("key", getKeySlot);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeSlots.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleSlot, mDescSlot, mDateTimeSlot, mKeySlot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleSlot = (TextView) itemView.findViewById(R.id.slot_title);
            mDescSlot = (TextView) itemView.findViewById(R.id.slot_desc);
            mDateTimeSlot = (TextView) itemView.findViewById(R.id.slot_date_time);
        }
    }

}