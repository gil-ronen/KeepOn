package com.gil_shiran_or.keepon.trainer_weekly_planner;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gil_shiran_or.keepon.R;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddNewSlotActivity extends AppCompatActivity {

    TextView mTitlePage;
    TextView mAddTitle;
    TextView mAddDesc;
    TextView mAddDays;
    TextView mAddTime;
    TextView mAddGroupSession;

    EditText mTitleSlot;
    EditText mDescSlot;
    EditText mFromTimeSlot;
    EditText mUntilTimeSlot;

    CheckBox mDay1;
    CheckBox mDay2;
    CheckBox mDay3;
    CheckBox mDay4;
    CheckBox mDay5;
    CheckBox mDay6;
    CheckBox mDay7;
    CheckBox mGroupSession;

    Button mBtnSaveSlot;
    Button mBtnCancel;

    DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_slot);

        mTitlePage = findViewById(R.id.add_titlePage);
        mAddTitle = findViewById(R.id.add_addTitle);
        mAddDesc = findViewById(R.id.add_addDesc);
        mAddDays = findViewById(R.id.add_days);
        mAddTime = findViewById(R.id.add_addTime);
        mAddGroupSession = findViewById(R.id.add_groupSession);

        mTitleSlot = findViewById(R.id.add_titleSlot);
        mDescSlot = findViewById(R.id.add_descSlot);
        mFromTimeSlot = findViewById(R.id.add_timeFromSlot);
        mUntilTimeSlot = findViewById(R.id.add_timeUntilSlot);

        mDay1 = findViewById(R.id.add_checkBox_day1);
        mDay2 = findViewById(R.id.add_checkBox_day2);
        mDay3 = findViewById(R.id.add_checkBox_day3);
        mDay4 = findViewById(R.id.add_checkBox_day4);
        mDay5 = findViewById(R.id.add_checkBox_day5);
        mDay6 = findViewById(R.id.add_checkBox_day6);
        mDay7 = findViewById(R.id.add_checkBox_day7);
        mGroupSession = findViewById(R.id.add_groupSession_checkBox);

        mBtnSaveSlot = findViewById(R.id.add_btnSaveSlot);
        mBtnCancel = findViewById(R.id.add_btnCancel);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("WeeklySchedule");

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewSlotActivity.this, MainWeeklyScheduleActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnSaveSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // insert data to database
                String title = mTitleSlot.getText().toString();
                String description = mDescSlot.getText().toString();
                String fromTimeSlot = mFromTimeSlot.getText().toString();
                String untilTimeSlot = mUntilTimeSlot.getText().toString();

                boolean day1 = mDay1.isChecked();
                boolean day2 = mDay2.isChecked();
                boolean day3 = mDay3.isChecked();
                boolean day4 = mDay4.isChecked();
                boolean day5 = mDay5.isChecked();
                boolean day6 = mDay6.isChecked();
                boolean day7 = mDay7.isChecked();
                boolean groupSession = mGroupSession.isChecked();

                if(day1)
                {
                    TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, mDay1.getHint().toString(),false, groupSession);
                    mDatabaseReference.push().setValue(timeSlot);
                }
                if(day2)
                {
                    TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, mDay2.getHint().toString(),false, groupSession);
                    mDatabaseReference.push().setValue(timeSlot);
                }
                if(day3)
                {
                    TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, mDay3.getHint().toString(),false, groupSession);
                    mDatabaseReference.push().setValue(timeSlot);
                }
                if(day4)
                {
                    TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, mDay4.getHint().toString(),false, groupSession);
                    mDatabaseReference.push().setValue(timeSlot);
                }
                if(day5)
                {
                    TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, mDay5.getHint().toString(),false, groupSession);
                    mDatabaseReference.push().setValue(timeSlot);
                }
                if(day6)
                {
                    TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, mDay6.getHint().toString(),false, groupSession);
                    mDatabaseReference.push().setValue(timeSlot);
                }
                if(day7)
                {
                    TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, mDay7.getHint().toString(),false, groupSession);
                    mDatabaseReference.push().setValue(timeSlot);
                }


                Intent intent = new Intent(AddNewSlotActivity.this, MainWeeklyScheduleActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);
        mAddTitle.setTypeface(MLight);
        mAddDesc.setTypeface(MLight);
        mAddDays.setTypeface(MLight);
        mAddTime.setTypeface(MLight);
        mAddGroupSession.setTypeface(MLight);

        mTitleSlot.setTypeface(MMedium);
        mDescSlot.setTypeface(MMedium);
        mFromTimeSlot.setTypeface(MMedium);
        mUntilTimeSlot.setTypeface(MMedium);

        mDay1.setTypeface(MMedium);
        mDay2.setTypeface(MMedium);
        mDay3.setTypeface(MMedium);
        mDay4.setTypeface(MMedium);
        mDay5.setTypeface(MMedium);
        mDay6.setTypeface(MMedium);
        mDay7.setTypeface(MMedium);
        mGroupSession.setTypeface(MMedium);

        mBtnSaveSlot.setTypeface(MMedium);
        mBtnCancel.setTypeface(MLight);
    }
}