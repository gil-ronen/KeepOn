package com.gil_shiran_or.keepon.trainer_weekly_planner;


import android.app.TimePickerDialog;

//import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gil_shiran_or.keepon.R;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import static java.util.Calendar.getInstance;


public class AddNewSlotActivity extends AppCompatActivity {

    TextView mTitlePage;
    TextView mAddTitle;
    TextView mAddDesc;
    TextView mAddDays;
    TextView mAddTime;
    TextView mAddGroupSession;

    EditText mTitleSlot;
    EditText mDescSlot;

    Button mFromTimeSlot;
    Button mUntilTimeSlot;

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

    TimePickerDialog timePickerDialog;
    int currentHour;
    int currentMinute;


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

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child("1EnOxPPh0cez6CzKnypPXvSZ1052").child("WeeklySchedule");


        final String dateForDB1 = getIntent().getExtras().getString("dateForDB1");
        final String dateForDB2 = getIntent().getExtras().getString("dateForDB2");
        final String dateForDB3 = getIntent().getExtras().getString("dateForDB3");
        final String dateForDB4 = getIntent().getExtras().getString("dateForDB4");
        final String dateForDB5 = getIntent().getExtras().getString("dateForDB5");
        final String dateForDB6 = getIntent().getExtras().getString("dateForDB6");
        final String dateForDB7 = getIntent().getExtras().getString("dateForDB7");

        final String dateForApp1 = getIntent().getExtras().getString("dateForApp1");
        final String dateForApp2 = getIntent().getExtras().getString("dateForApp2");
        final String dateForApp3 = getIntent().getExtras().getString("dateForApp3");
        final String dateForApp4 = getIntent().getExtras().getString("dateForApp4");
        final String dateForApp5 = getIntent().getExtras().getString("dateForApp5");
        final String dateForApp6 = getIntent().getExtras().getString("dateForApp6");
        final String dateForApp7 = getIntent().getExtras().getString("dateForApp7");

        mDay1.setHint(dateForApp1);
        mDay2.setHint(dateForApp2);
        mDay3.setHint(dateForApp3);
        mDay4.setHint(dateForApp4);
        mDay5.setHint(dateForApp5);
        mDay6.setHint(dateForApp6);
        mDay7.setHint(dateForApp7);


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

                if (checkValidInput()) {
                    // insert data to database
                    String title = mTitleSlot.getText().toString();
                    String description = mDescSlot.getText().toString();
                    //String fromTimeSlot = mFromTimeSlot.getHint().toString();
                    //String untilTimeSlot = mUntilTimeSlot.getHint().toString();
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


                    if (day1) {
                        TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, dateForDB1, "" ,false, groupSession);
                        mDatabaseReference.child(dateForDB1).push().setValue(timeSlot);
                    }
                    if (day2) {
                        TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, dateForDB2, "" ,false, groupSession);
                        mDatabaseReference.child(dateForDB2).push().setValue(timeSlot);
                    }
                    if (day3) {
                        TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, dateForDB3, "",false, groupSession);
                        mDatabaseReference.child(dateForDB3).push().setValue(timeSlot);
                    }
                    if (day4) {
                        TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, dateForDB4, "",false, groupSession);
                        mDatabaseReference.child(dateForDB4).push().setValue(timeSlot);
                    }
                    if (day5) {
                        TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, dateForDB5, "",false, groupSession);
                        mDatabaseReference.child(dateForDB5).push().setValue(timeSlot);
                    }
                    if (day6) {
                        TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, dateForDB6, "",false, groupSession);
                        mDatabaseReference.child(dateForDB6).push().setValue(timeSlot);
                    }
                    if (day7) {
                        TimeSlot timeSlot = new TimeSlot(title, description, fromTimeSlot, untilTimeSlot, dateForDB7, "",false, groupSession);
                        mDatabaseReference.child(dateForDB7).push().setValue(timeSlot);
                    }


                    Intent intent = new Intent(AddNewSlotActivity.this, MainWeeklyScheduleActivity.class);
                    startActivity(intent);
                    finish();

                }
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


        mFromTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(AddNewSlotActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        mFromTimeSlot.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        mFromTimeSlot.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }, currentHour, currentMinute, true);

                timePickerDialog.show();
            }
        });

        mUntilTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(AddNewSlotActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        mUntilTimeSlot.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        mUntilTimeSlot.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }, currentHour, currentMinute, true);

                timePickerDialog.show();
            }
        });


    }



    private boolean checkValidInput() {

        // Reset errors displayed in the form.
        mTitleSlot.setError(null);
        mFromTimeSlot.setError(null);
        mUntilTimeSlot.setError(null);
        mAddDays.setError(null);

        // Store values at the time of the onClick attempt.
        String title = mTitleSlot.getText().toString();
        String fromTimeSlot = mFromTimeSlot.getText().toString();
        String untilTimeSlot = mUntilTimeSlot.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(title)) {
            mTitleSlot.setError(getString(R.string.error_field_required));
            focusView = mTitleSlot;
            cancel = true;
        }

        if (TextUtils.isEmpty(fromTimeSlot)) {
            mFromTimeSlot.setError(getString(R.string.error_field_required));
            focusView = mFromTimeSlot;
            cancel = true;
        }

        if (TextUtils.isEmpty(untilTimeSlot)) {
            mUntilTimeSlot.setError(getString(R.string.error_field_required));
            focusView = mUntilTimeSlot;
            cancel = true;
        }

        if((!mDay1.isChecked())&&(!mDay2.isChecked())&&(!mDay3.isChecked())&&(!mDay4.isChecked())&&(!mDay5.isChecked())&&(!mDay6.isChecked())&&(!mDay7.isChecked()))
        {
            mAddDays.setError(getString(R.string.error_field_required));
            focusView = mAddDays;
            cancel = true;
        }

        if (cancel) {
            // There was an error.
            // form field with an error.
            focusView.requestFocus();
            Toast.makeText(this, "Please Verify All Field", Toast.LENGTH_SHORT).show();
        }
        else {
            return true;
        }
        return false;
    }

}