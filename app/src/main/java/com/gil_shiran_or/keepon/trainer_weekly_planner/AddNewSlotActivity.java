package com.gil_shiran_or.keepon.trainer_weekly_planner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gil_shiran_or.keepon.R;



import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;

public class AddNewSlotActivity extends AppCompatActivity {

    TextView mTitlePage;
    TextView mAddTitle;
    TextView mAddDesc;
    TextView mAddDate;

    EditText mTitleSlot;
    EditText mDescSlot;
    EditText mDateAndTimeSlot;

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
        mAddDate = findViewById(R.id.add_addDate);

        mTitleSlot = findViewById(R.id.add_titleSlot);
        mDescSlot = findViewById(R.id.add_descSlot);
        mDateAndTimeSlot = findViewById(R.id.add_dateSlot);

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
                String dateAndTime = mDateAndTimeSlot.getText().toString();
                String description = mDescSlot.getText().toString();

                TimeSlot timeSlot = new TimeSlot(title, dateAndTime, description);
                mDatabaseReference.push().setValue(timeSlot);

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
        mTitleSlot.setTypeface(MMedium);

        mAddDesc.setTypeface(MLight);
        mDescSlot.setTypeface(MMedium);

        mAddDate.setTypeface(MLight);
        mDateAndTimeSlot.setTypeface(MMedium);

        mBtnSaveSlot.setTypeface(MMedium);
        mBtnCancel.setTypeface(MLight);
    }
}