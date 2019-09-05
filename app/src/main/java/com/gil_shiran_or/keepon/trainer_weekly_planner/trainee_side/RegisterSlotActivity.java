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
    TextView mTraineeAreRegisteredLabel;
    TextView mTitle;
    TextView mTrainerName;
    TextView mDescription;
    TextView mDateDay;
    TextView mFromToTime;
    TextView mIsGroupLabel;
    TextView mNumberOfAvailablePlaces;

    Button mBtnDoAction;
    Button mBtnCancel;

    DatabaseReference mTrainerScheduleDatabaseReference;
    DatabaseReference mTrainerDatabaseReference;
    String mTrainerId;
    String mCurrentTraineeId;

    private String mDateForApp;
    private String mDateForDB;

    int currentHour;
    int currentMinute;

    private String mTrainerFullName;


    private String mKeySlot;
    private String mTraineeId; //TODO: Fetch the list of registered trainees Id's

    private Boolean mIsOccupied;
    private Boolean mIsGroupSession;
    private int mCurrentSumPeopleInGroup;
    private int mGroupLimit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_slot);


        mTitlePage = findViewById(R.id.register_titlePage);
        mTraineeAreRegisteredLabel = findViewById(R.id.register_traineeAreRegistered);
        mTitle = findViewById(R.id.register_title);
        mTrainerName = findViewById(R.id.register_trainerName);
        mDescription = findViewById(R.id.register_desc);
        mDateDay = findViewById(R.id.register_date);
        mFromToTime = findViewById(R.id.register_fromAndToTime);
        mIsGroupLabel = findViewById(R.id.register_isGroupSession);
        mNumberOfAvailablePlaces = findViewById(R.id.register_numberOfRegistered);

        mBtnDoAction = findViewById(R.id.register_btnDoAction);
        mBtnCancel = findViewById(R.id.register_btnCancel);


        //mTrainerName.setText(getIntent().getExtras().getString("trainerName"));
        mTitle.setText(getIntent().getExtras().getString("title"));
        mDescription.setText(getIntent().getExtras().getString("description"));
        mDateDay.setText(getIntent().getExtras().getString("dateForApp"));
        mFromToTime.setText("From " + getIntent().getExtras().getString("timeFrom") + " To " + getIntent().getExtras().getString("timeUntil"));
        mDateForDB = getIntent().getExtras().getString("dateForDB");
        mKeySlot = getIntent().getExtras().getString("key");
        mCurrentTraineeId = getIntent().getExtras().getString("currentTraineeId");
        mTraineeId = getIntent().getExtras().getString("traineeId"); //TODO: Fetch the list of registered trainees Id's
        mTrainerId = getIntent().getExtras().getString("trainerId");
        mIsOccupied =  getIntent().getExtras().getBoolean("isOccupied");
        mIsGroupSession =  getIntent().getExtras().getBoolean("isGroupSession");
        mCurrentSumPeopleInGroup = getIntent().getExtras().getInt("currentSumPeopleInGroup");
        mGroupLimit = getIntent().getExtras().getInt("groupLimit");


        mTrainerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(mTrainerId);
        mTrainerScheduleDatabaseReference = mTrainerDatabaseReference.child("WeeklySchedule").child(mDateForDB).child(mKeySlot);


        //TODO: check if trainee exist in list of registered trainees Id's
        if(mCurrentTraineeId.equals(mTraineeId))
        {
            mTraineeAreRegisteredLabel.setVisibility(View.VISIBLE);
            mBtnDoAction.setText("Delete registration");
        }

        if(mIsGroupSession)
        {
            if(mIsOccupied)
            {
                mNumberOfAvailablePlaces.setText("All " + mGroupLimit + " places are occupied");
            }
            else
            {
                mNumberOfAvailablePlaces.setText((mGroupLimit - mCurrentSumPeopleInGroup) + " places left out of " + mGroupLimit);
            }
            mIsGroupLabel.setText("It's a group training");
            mNumberOfAvailablePlaces.setVisibility(View.VISIBLE);
        }

        getTrainerFullName();


        mBtnDoAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCurrentTraineeId.equals(mTraineeId))
                {
                    DeleteCurrentTraineeFromTheSlot();
                }
                else
                {
                    RegisterCurrentTraineeToTheSlot();
                }

            }
        });


        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterSlotActivity.this, MainWeeklySlotsPickerActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTraineeAreRegisteredLabel.setTypeface(MMedium);
        mTitlePage.setTypeface(MMedium);
        mTitle.setTypeface(MMedium);
        mTrainerName.setTypeface(MMedium);
        mDescription.setTypeface(MLight);
        mDateDay.setTypeface(MLight);
        mFromToTime.setTypeface(MLight);
        mBtnDoAction.setTypeface(MMedium);
        mBtnCancel.setTypeface(MLight);
        mIsGroupLabel.setTypeface(MLight);
        mNumberOfAvailablePlaces.setTypeface(MLight);




    }


    public void RegisterCurrentTraineeToTheSlot()
    {
        mTrainerScheduleDatabaseReference.child("currentSumPeopleInGroup").setValue(mCurrentSumPeopleInGroup + 1);
        mTrainerScheduleDatabaseReference.child("traineeId").setValue(mCurrentTraineeId); //TODO: to add trainee to list of registered trainees Id's

        if((mCurrentSumPeopleInGroup + 1) == mGroupLimit)
            mTrainerScheduleDatabaseReference.child("occupied").setValue(true);

        Intent intent = new Intent(RegisterSlotActivity.this, MainWeeklySlotsPickerActivity.class);
        startActivity(intent);
        finish();

    }

    public void DeleteCurrentTraineeFromTheSlot()
    {
        mTrainerScheduleDatabaseReference.child("currentSumPeopleInGroup").setValue(mCurrentSumPeopleInGroup - 1);
        mTrainerScheduleDatabaseReference.child("traineeId").setValue(""); //TODO: to delete trainee to list of registered trainees Id's
        mTrainerScheduleDatabaseReference.child("occupied").setValue(false);

        Intent intent = new Intent(RegisterSlotActivity.this, MainWeeklySlotsPickerActivity.class);
        startActivity(intent);
        finish();

    }


    public void getTrainerFullName()
    {
        final DatabaseReference current_user_db = mTrainerDatabaseReference.child("Profile").child("name");
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mTrainerFullName = dataSnapshot.getValue(String.class);
                mTrainerName.setText("with " + mTrainerFullName);
                current_user_db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

}

