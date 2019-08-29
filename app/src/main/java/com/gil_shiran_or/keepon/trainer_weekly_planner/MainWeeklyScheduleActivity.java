package com.gil_shiran_or.keepon.trainer_weekly_planner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainWeeklyScheduleActivity extends AppCompatActivity {

    TextView mTitlePage;
    TextView mSubtitlePage;
    TextView mEndPage;
    Button mBtnAddNew;

    DatabaseReference databaseReference;
    RecyclerView timeSlotsRecyclerView;
    ArrayList<TimeSlot> slotsList;
    TimeSlotsAdapter timeSlotsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weekly_schedule);

        mTitlePage = findViewById(R.id.main_titlePage);
        mSubtitlePage = findViewById(R.id.main_subtitlePage);
        mEndPage = findViewById(R.id.main_endPage);
        mBtnAddNew = findViewById(R.id.main_btnAddNew);

        mBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWeeklyScheduleActivity.this, AddNewSlotActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        // working with data
        timeSlotsRecyclerView = findViewById(R.id.main_timeSlots);
        timeSlotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        slotsList = new ArrayList<TimeSlot>();

        // get data from firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("WeeklySchedule");

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set code to retrive data and replace layout

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    TimeSlot slot = dataSnapshot1.getValue(TimeSlot.class);
                    slotsList.add(slot);
                }

                timeSlotsAdapter = new TimeSlotsAdapter(MainWeeklyScheduleActivity.this, slotsList);
                timeSlotsRecyclerView.setAdapter(timeSlotsAdapter);
                timeSlotsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
        */

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(slotsList.isEmpty())
                {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        TimeSlot slot = dataSnapshot1.getValue(TimeSlot.class);
                        slotsList.add(slot);
                    }
                }
                else
                {
                    TimeSlot slot = dataSnapshot.getValue(TimeSlot.class);
                    slotsList.add(slot);
                }
                timeSlotsAdapter = new TimeSlotsAdapter(MainWeeklyScheduleActivity.this, slotsList);
                timeSlotsRecyclerView.setAdapter(timeSlotsAdapter);
                timeSlotsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);
        mSubtitlePage.setTypeface(MLight);
        mEndPage.setTypeface(MLight);
        mBtnAddNew.setTypeface(MLight);
    }
}

