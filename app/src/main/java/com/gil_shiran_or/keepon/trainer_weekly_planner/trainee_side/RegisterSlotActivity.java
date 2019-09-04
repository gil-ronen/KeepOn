package com.gil_shiran_or.keepon.trainer_weekly_planner.trainee_side;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterSlotActivity extends AppCompatActivity {

    TextView mTitlePage;
    TextView mTitle;
    TextView mTrainerName;
    TextView mDescription;
    TextView mDateDay;
    TextView mTimeFrom;
    TextView mTimeTo;

    Button mBtnRegister;
    Button mBtnCancel;

    DatabaseReference mTrainerScheduleDatabaseReference;
    DatabaseReference mTrainerDatabaseReference;
    String mTrainerId;

    private String mDateForApp;
    private String mDateForDB;

    int currentHour;
    int currentMinute;

    private String mTrainerFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_slot);


        mTitlePage = findViewById(R.id.register_titlePage);
        mTitle = findViewById(R.id.register_title);
        mTrainerName = findViewById(R.id.register_trainerName);
        mDescription = findViewById(R.id.register_desc);
        mDateDay = findViewById(R.id.register_date);
        mTimeFrom = findViewById(R.id.register_fromTime);
        mTimeTo = findViewById(R.id.register_toTime);

        Button mBtnRegister = findViewById(R.id.register_btnRegister);
        Button mBtnCancel = findViewById(R.id.register_btnCancel);


        mTitle.setText(getIntent().getExtras().getString("title"));
        mTrainerName.setText(getIntent().getExtras().getString("trainerName"));
        mDescription.setText(getIntent().getExtras().getString("description"));
        mDateDay.setText(getIntent().getExtras().getString("dateForApp"));
        mTimeFrom.setText(getIntent().getExtras().getString("timeFrom"));
        mTimeTo.setText(getIntent().getExtras().getString("timeUntil"));
        mDateForDB = getIntent().getExtras().getString("dateForDB");
        //mDateForApp = getIntent().getExtras().getString("dateForApp");
        final String mKeySlot = getIntent().getExtras().getString("key");
        final String traineeId = getIntent().getExtras().getString("traineeId");

        //TODO: TRAINER ID NEED TO TAKEN FROM CURRENT USER FROM DB!!!
        mTrainerId = "ayAWQUYKUZbISD7FicSJvYOWShE3";
        mTrainerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(mTrainerId);
        mTrainerScheduleDatabaseReference = mTrainerDatabaseReference.child("WeeklySchedule").child(mDateForDB).child(mKeySlot);

        //TODO:
        getTrainerFullName();

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterSlotActivity.this, MainWeeklySlotsPickerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // update data in database

                mTrainerScheduleDatabaseReference.child("traineeId").setValue(traineeId);
                mTrainerScheduleDatabaseReference.child("occupied").setValue(true);

                Intent intent = new Intent(RegisterSlotActivity.this, MainWeeklySlotsPickerActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);
        mTitle.setTypeface(MMedium);
        mTrainerName.setTypeface(MMedium);
        mDescription.setTypeface(MMedium);
        mDateDay.setTypeface(MMedium);
        mTimeFrom.setTypeface(MMedium);
        mTimeTo.setTypeface(MMedium);
        mBtnRegister.setTypeface(MMedium);
        mBtnCancel.setTypeface(MLight);


    }

    public void getTrainerFullName()
    {
        final DatabaseReference current_user_db = mTrainerDatabaseReference.child("Profile").child("name");
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mTrainerFullName = dataSnapshot.getValue(String.class);
                mTrainerName.setText(mTrainerFullName);
                current_user_db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

}

