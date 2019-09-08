package com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.create_and_edit_time_slots;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gil_shiran_or.keepon.R;


import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
    EditText mGroupSizeLimit;

    Button mFromTimeSlot;
    Button mUntilTimeSlot;

    CheckBox mDay;
    CheckBox mGroupSession;

    Button mBtnSaveUpdate;
    Button mBtnDelete;

    DatabaseReference mDatabaseReference;
    String mTrainerId;

    private String mDateForApp;
    private String mDateForDB;
    private int mGroupLimit;
    private int mCurrentSumPeopleInGroup;

    TimePickerDialog timePickerDialog;
    int currentHour;
    int currentMinute;


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
        mGroupSizeLimit = findViewById(R.id.edit_groupLimit);

        mBtnSaveUpdate = findViewById(R.id.edit_btnSaveUpdate);
        mBtnDelete = findViewById(R.id.edit_btnDelete);


/*
        mTitle.setText(getIntent().getExtras().getString("title"));
        mDescription.setText(getIntent().getExtras().getString("description"));
        mDateDay.setText(getIntent().getExtras().getString("dateForApp"));
        mTimeFrom.setText("From " + getIntent().getExtras().getString("timeFrom"));
        mTimeTo.setText("To " + getIntent().getExtras().getString("timeUntil"));
        mDateForDB = getIntent().getExtras().getString("dateForDB");
        mKeySlot = getIntent().getExtras().getString("key");
        mCurrentTraineeId = getIntent().getExtras().getString("currentTraineeId");
        mTraineeId = getIntent().getExtras().getString("traineeId"); //TODO: Fetch the list of registered trainees Id's
        mTrainerId = getIntent().getExtras().getString("trainerId");
        mIsOccupied =  getIntent().getExtras().getBoolean("isOccupied");
        mIsGroupSession =  getIntent().getExtras().getBoolean("isGroupSession");
        mCurrentSumPeopleInGroup = getIntent().getExtras().getInt("currentSumPeopleInGroup");
        mGroupLimit = getIntent().getExtras().getInt("groupLimit");

        */



        mDateForApp = getIntent().getExtras().getString("dateForApp");
        mDateForDB = getIntent().getExtras().getString("dateForDB");
        final String mKeySlot = getIntent().getExtras().getString("key");
        mTitleSlot.setText(getIntent().getExtras().getString("title"));
        mDescSlot.setText(getIntent().getExtras().getString("description"));
        mFromTimeSlot.setText(getIntent().getExtras().getString("timeFrom"));
        mUntilTimeSlot.setText(getIntent().getExtras().getString("timeUntil"));
        mDay.setHint(getIntent().getExtras().getString("dateForApp"));
        mCurrentSumPeopleInGroup = getIntent().getExtras().getInt("currentSumPeopleInGroup");
        mGroupSession.setChecked(getIntent().getExtras().getBoolean("isGroupSession"));
        int groupLimit = getIntent().getExtras().getInt("groupLimit");

        if(mGroupSession.isChecked())
        {
            mGroupSizeLimit.setEnabled(true);
            mGroupSizeLimit.setText(""+groupLimit);

            if(mCurrentSumPeopleInGroup > 1)
            {
                mGroupSession.setEnabled(false);
            }
        }



        //TODO: TRAINER ID NEED TO TAKEN FROM CURRENT USER FROM DB!!!
        mTrainerId = "ayAWQUYKUZbISD7FicSJvYOWShE3";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(mTrainerId).child("WeeklySchedule").child(mDateForDB).child(mKeySlot);



        mGroupSession.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mGroupSizeLimit.setEnabled(isChecked);
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDeleteDialog();

            }
        });

        mBtnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidInput()) {
                    // update data in database
                    String title = mTitleSlot.getText().toString();
                    String description = mDescSlot.getText().toString();
                    String fromTimeSlot = mFromTimeSlot.getText().toString();
                    String untilTimeSlot = mUntilTimeSlot.getText().toString();
                    String groupSizeLimit = mGroupSizeLimit.getText().toString();
                    int groupLimit;
                    boolean groupSession = mGroupSession.isChecked();

                    if(groupSession)
                    {
                        groupLimit = Integer.parseInt(groupSizeLimit);
                    }
                    else
                    {
                        groupLimit = 1;
                    }

                    if(groupLimit <= mCurrentSumPeopleInGroup)
                    {
                        mDatabaseReference.child("occupied").setValue(true);
                    }
                    else
                    {
                        mDatabaseReference.child("occupied").setValue(false);
                    }

                    mDatabaseReference.child("title").setValue(title);
                    mDatabaseReference.child("description").setValue(description);
                    mDatabaseReference.child("timeFrom").setValue(fromTimeSlot);
                    mDatabaseReference.child("timeUntil").setValue(untilTimeSlot);
                    mDatabaseReference.child("groupSession").setValue(groupSession);
                    mDatabaseReference.child("groupLimit").setValue(groupLimit);

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

        mDay.setTypeface(MMedium);

        mGroupSession.setTypeface(MMedium);

        mBtnSaveUpdate.setTypeface(MMedium);
        mBtnDelete.setTypeface(MLight);



        mFromTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = new TimePickerDialog(EditSlotActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        //mFromTimeSlot.setHint(String.format("%02d:%02d", hourOfDay, minutes));
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
                timePickerDialog = new TimePickerDialog(EditSlotActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        //mUntilTimeSlot.setHint(String.format("%02d:%02d", hourOfDay, minutes));
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
        mGroupSizeLimit.setError(null);

        // Store values at the time of the onClick attempt.
        String title = mTitleSlot.getText().toString();
        String fromTimeSlot = mFromTimeSlot.getText().toString();
        String untilTimeSlot = mUntilTimeSlot.getText().toString();
        String stringGroupLimit = mGroupSizeLimit.getText().toString();
        int numberGroupLimit = 1;


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


        if(mGroupSession.isChecked())
        {
            if(TextUtils.isEmpty(stringGroupLimit))
            {
                mGroupSizeLimit.setError(getString(R.string.error_field_required));
                focusView = mGroupSizeLimit;
                cancel = true;
            }
            else
            {
                numberGroupLimit = Integer.parseInt(stringGroupLimit);
                if(numberGroupLimit < 2)
                {
                    mGroupSizeLimit.setError("The minimum limit must be over 2 people in a group");
                    focusView = mGroupSizeLimit;
                    cancel = true;
                }
            }

        }

        if(numberGroupLimit < mCurrentSumPeopleInGroup)
        {
            mGroupSizeLimit.setError("There are already " + mCurrentSumPeopleInGroup + "  registered trainees. The minimum limit must be over " + mCurrentSumPeopleInGroup + ".");
            focusView = mGroupSizeLimit;
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

    private void showDeleteDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete training")
                .setMessage("Are you sure you want to delete this training?\n" +
                        "If registered trainees participate in this training, it will automatically cancel them.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabaseReference.removeValue();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}