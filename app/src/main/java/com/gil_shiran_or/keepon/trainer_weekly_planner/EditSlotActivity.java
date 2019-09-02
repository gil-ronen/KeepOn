package com.gil_shiran_or.keepon.trainer_weekly_planner;

import android.support.annotation.NonNull;
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

public class EditSlotActivity extends AppCompatActivity {

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

    CheckBox mDay;
    CheckBox mGroupSession;

    Button mBtnSaveUpdate;
    Button mBtnDelete;

    DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_slot);

        mTitlePage = findViewById(R.id.edit_titlePage);
        mAddTitle = findViewById(R.id.edit_addTitle);
        mAddDesc = findViewById(R.id.edit_addDesc);
        mAddDays = findViewById(R.id.edit_days);
        mAddTime = findViewById(R.id.edit_addTime);
        mAddGroupSession = findViewById(R.id.edit_groupSession);

        mTitleSlot = findViewById(R.id.edit_titleSlot);
        mDescSlot = findViewById(R.id.edit_descSlot);
        mFromTimeSlot = findViewById(R.id.edit_timeFromSlot);
        mUntilTimeSlot = findViewById(R.id.edit_timeUntilSlot);

        mDay = findViewById(R.id.edit_checkBox_day);
        mGroupSession = findViewById(R.id.edit_groupSession_checkBox);

        mBtnSaveUpdate = findViewById(R.id.edit_btnSaveUpdate);
        mBtnDelete = findViewById(R.id.edit_btnDelete);

        final String mKeySlot = getIntent().getExtras().getString("key");
        mTitleSlot.setText(getIntent().getExtras().getString("title"));
        mDescSlot.setText(getIntent().getExtras().getString("description"));
        mFromTimeSlot.setText(getIntent().getExtras().getString("timeFrom"));
        mUntilTimeSlot.setText(getIntent().getExtras().getString("timeUntil"));
        mDay.setHint(getIntent().getExtras().getString("day"));
        mGroupSession.setChecked(getIntent().getExtras().getBoolean("isGroupSession"));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("WeeklySchedule").child(mKeySlot);


        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseReference.removeValue();
                Intent intent = new Intent(EditSlotActivity.this, MainWeeklyScheduleActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update data in database
                String title = mTitleSlot.getText().toString();
                String description = mDescSlot.getText().toString();
                String fromTimeSlot = mFromTimeSlot.getText().toString();
                String untilTimeSlot = mUntilTimeSlot.getText().toString();

                String day = mDay.getHint().toString();
                boolean groupSession = mGroupSession.isChecked();

                TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, day,false, groupSession);
                mDatabaseReference.setValue(timeSlot);

                Intent intent = new Intent(EditSlotActivity.this, MainWeeklyScheduleActivity.class);
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

        mDay.setTypeface(MMedium);

        mGroupSession.setTypeface(MMedium);

        mBtnSaveUpdate.setTypeface(MMedium);
        mBtnDelete.setTypeface(MLight);
    }
}