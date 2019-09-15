package com.gil_shiran_or.keepon.trainings_weekly_schedule.trainee_side;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.TraineeRegisterTimeSlot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


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


    private String mTrainerFullName;


    private String mKeySlot;

    private Boolean mIsOccupied;
    private Boolean mIsGroupSession;
    private boolean isTraineeExistInThisSlot;
    private int mCurrentSumPeopleInGroup;
    private int mGroupLimit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_slot);

        getWindow().setBackgroundDrawableResource(R.drawable.background_profile_blur);

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
        //mTraineeId = getIntent().getExtras().getString("traineeId"); //TODO: Fetch the list of registered trainees Id's
        mTrainerId = getIntent().getExtras().getString("trainerId");
        mIsOccupied =  getIntent().getExtras().getBoolean("isOccupied");
        mIsGroupSession =  getIntent().getExtras().getBoolean("isGroupSession");
        mCurrentSumPeopleInGroup = getIntent().getExtras().getInt("currentSumPeopleInGroup");
        mGroupLimit = getIntent().getExtras().getInt("groupLimit");
        isTraineeExistInThisSlot = getIntent().getExtras().getBoolean("isTraineeExistInThisSlot");


        mTrainerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(mTrainerId);
        mTrainerScheduleDatabaseReference = mTrainerDatabaseReference.child("WeeklySchedule").child(mDateForDB).child(mKeySlot);


        //TODO: check if trainee exist in list of registered trainees Id's
        //if(mCurrentTraineeId.equals(mTraineeId))
        if(isTraineeExistInThisSlot)
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
                if((mGroupLimit - mCurrentSumPeopleInGroup) == 1)
                    mNumberOfAvailablePlaces.setText("There is only one place left!");
                else
                    mNumberOfAvailablePlaces.setText((mGroupLimit - mCurrentSumPeopleInGroup) + " places left out of " + mGroupLimit);
            }
            mIsGroupLabel.setText("It's a group training");
            mNumberOfAvailablePlaces.setVisibility(View.VISIBLE);
        }

        getTrainerFullName();


        mBtnDoAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isTraineeExistInThisSlot)
                {
                    showCancelDialog();
                }
                else
                {
                    showRegistrationDialog();
                }

            }
        });


        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        //TODO: to add trainee to list of registered trainees Id's

        String key = mTrainerScheduleDatabaseReference.child("traineesId").push().getKey();
        TraineeRegisterTimeSlot traineeRegisterTimeSlot = new TraineeRegisterTimeSlot(mCurrentTraineeId);
        Map<String, Object> traineeRegisterTimeSlotMap = traineeRegisterTimeSlot.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key , traineeRegisterTimeSlotMap);

        mTrainerScheduleDatabaseReference.child("traineesId").updateChildren(childUpdates);


        mTrainerScheduleDatabaseReference.child("currentSumPeopleInGroup").setValue(mCurrentSumPeopleInGroup + 1);
        if((mCurrentSumPeopleInGroup + 1) >= mGroupLimit)
            mTrainerScheduleDatabaseReference.child("occupied").setValue(true);



        finish();

    }

    public void DeleteCurrentTraineeFromTheSlot()
    {
        //TODO: to delete trainee to list of registered trainees Id's

        DatabaseReference databaseReference = mTrainerScheduleDatabaseReference.child("traineesId");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if(ds.child("userId").getValue(String.class).equals(mCurrentTraineeId))
                    {
                        mTrainerScheduleDatabaseReference.child("traineesId").child(ds.getKey()).removeValue();
                        break;
                    }
                }
                mTrainerScheduleDatabaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mTrainerScheduleDatabaseReference.child("currentSumPeopleInGroup").setValue(mCurrentSumPeopleInGroup - 1);
        mTrainerScheduleDatabaseReference.child("occupied").setValue(false);


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



    // Show error on screen with an alert dialog
    private void showRegistrationDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Registration for this training")
                .setMessage("Do you want to register for this training?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RegisterCurrentTraineeToTheSlot();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void showCancelDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Cancel training")
                .setMessage("Are you sure you want to cancel training?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteCurrentTraineeFromTheSlot();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
