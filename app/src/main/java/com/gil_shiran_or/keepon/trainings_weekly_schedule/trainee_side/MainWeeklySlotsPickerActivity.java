package com.gil_shiran_or.keepon.trainings_weekly_schedule.trainee_side;


import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainWeeklySlotsPickerActivity extends AppCompatActivity {

    private TextView mTitlePage;
    private TextView mSubtitlePage;
    private TextView mEndPage;

    private RecyclerView mTimeSlotsRecyclerView1;
    private RecyclerView mTimeSlotsRecyclerView2;
    private RecyclerView mTimeSlotsRecyclerView3;
    private RecyclerView mTimeSlotsRecyclerView4;
    private RecyclerView mTimeSlotsRecyclerView5;
    private RecyclerView mTimeSlotsRecyclerView6;
    private RecyclerView mTimeSlotsRecyclerView7;

    private ExpandableViewGroup expandableViewGroup1;
    private ExpandableViewGroup expandableViewGroup2;
    private ExpandableViewGroup expandableViewGroup3;
    private ExpandableViewGroup expandableViewGroup4;
    private ExpandableViewGroup expandableViewGroup5;
    private ExpandableViewGroup expandableViewGroup6;
    private ExpandableViewGroup expandableViewGroup7;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String mTrainerId;


    private TraineeTimeSlotsAdapter traineeTimeSlotsAdapter1;
    private TraineeTimeSlotsAdapter traineeTimeSlotsAdapter2;
    private TraineeTimeSlotsAdapter traineeTimeSlotsAdapter3;
    private TraineeTimeSlotsAdapter traineeTimeSlotsAdapter4;
    private TraineeTimeSlotsAdapter traineeTimeSlotsAdapter5;
    private TraineeTimeSlotsAdapter traineeTimeSlotsAdapter6;
    private TraineeTimeSlotsAdapter traineeTimeSlotsAdapter7;

    private String dateForApp1;
    private String dateForDB1;
    private String dateForApp2;
    private String dateForDB2;
    private String dateForApp3;
    private String dateForDB3;
    private String dateForApp4;
    private String dateForDB4;
    private String dateForApp5;
    private String dateForDB5;
    private String dateForApp6;
    private String dateForDB6;
    private String dateForApp7;
    private String dateForDB7;

    private String mTrainerFullName;

    private String mTraineeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weekly_slots_picker);

        mTitlePage = findViewById(R.id.mainPicker_titlePage);
        mSubtitlePage = findViewById(R.id.mainPicker_subtitlePage);
        mEndPage = findViewById(R.id.mainPicker_endPage);

        //TODO: TRAINER ID NEED TO TAKEN FROM CURRENT USER FROM DB!!!
        mTrainerId = getIntent().getExtras().getString("trainerId");
        mAuth = FirebaseAuth.getInstance();
        mTraineeId = mAuth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(mTrainerId);

        getTrainerFullName();
        synchronizeDates();

        mTimeSlotsRecyclerView1 = findViewById(R.id.mainPicker_timeSlotsList1);
        mTimeSlotsRecyclerView1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView1.setLayoutManager(layoutManager1);

        ViewGroup day1SlotsExpanderViewGroup = findViewById(R.id.mainPicker_expandableDay1);
        ViewGroup day1SlotsViewGroup = mTimeSlotsRecyclerView1;
        View day1SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day1SlotsExpanderViewGroup, false);
        day1SlotsExpanderViewGroup.addView(day1SlotsExpandableLayoutView);
        expandableViewGroup1 = new ExpandableViewGroup(dateForApp1, dateForApp1, (ViewGroup) day1SlotsExpandableLayoutView, day1SlotsViewGroup);
        expandableViewGroup1.changeArrow();

        mTimeSlotsRecyclerView2 = findViewById(R.id.mainPicker_timeSlotsList2);
        mTimeSlotsRecyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView2.setLayoutManager(layoutManager2);

        ViewGroup day2SlotsExpanderViewGroup = findViewById(R.id.mainPicker_expandableDay2);
        ViewGroup day2SlotsViewGroup = mTimeSlotsRecyclerView2;
        View day2SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day2SlotsExpanderViewGroup, false);
        day2SlotsExpanderViewGroup.addView(day2SlotsExpandableLayoutView);
        expandableViewGroup2 = new ExpandableViewGroup(dateForApp2, dateForApp2, (ViewGroup) day2SlotsExpandableLayoutView, day2SlotsViewGroup);


        mTimeSlotsRecyclerView3 = findViewById(R.id.mainPicker_timeSlotsList3);
        mTimeSlotsRecyclerView3.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView3.setLayoutManager(layoutManager3);

        ViewGroup day3SlotsExpanderViewGroup = findViewById(R.id.mainPicker_expandableDay3);
        ViewGroup day3SlotsViewGroup = mTimeSlotsRecyclerView3;
        View day3SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day3SlotsExpanderViewGroup, false);
        day3SlotsExpanderViewGroup.addView(day3SlotsExpandableLayoutView);
        expandableViewGroup3 = new ExpandableViewGroup(dateForApp3, dateForApp3, (ViewGroup) day3SlotsExpandableLayoutView, day3SlotsViewGroup);


        mTimeSlotsRecyclerView4 = findViewById(R.id.mainPicker_timeSlotsList4);
        mTimeSlotsRecyclerView4.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView4.setLayoutManager(layoutManager4);

        ViewGroup day4SlotsExpanderViewGroup = findViewById(R.id.mainPicker_expandableDay4);
        ViewGroup day4SlotsViewGroup = mTimeSlotsRecyclerView4;
        View day4SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day4SlotsExpanderViewGroup, false);
        day4SlotsExpanderViewGroup.addView(day4SlotsExpandableLayoutView);
        expandableViewGroup4 = new ExpandableViewGroup(dateForApp4, dateForApp4, (ViewGroup) day4SlotsExpandableLayoutView, day4SlotsViewGroup);


        mTimeSlotsRecyclerView5 = findViewById(R.id.mainPicker_timeSlotsList5);
        mTimeSlotsRecyclerView5.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView5.setLayoutManager(layoutManager5);

        ViewGroup day5SlotsExpanderViewGroup = findViewById(R.id.mainPicker_expandableDay5);
        ViewGroup day5SlotsViewGroup = mTimeSlotsRecyclerView5;
        View day5SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day5SlotsExpanderViewGroup, false);
        day5SlotsExpanderViewGroup.addView(day5SlotsExpandableLayoutView);
        expandableViewGroup5 = new ExpandableViewGroup(dateForApp5, dateForApp5, (ViewGroup) day5SlotsExpandableLayoutView, day5SlotsViewGroup);


        mTimeSlotsRecyclerView6 = findViewById(R.id.mainPicker_timeSlotsList6);
        mTimeSlotsRecyclerView6.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView6.setLayoutManager(layoutManager6);

        ViewGroup day6SlotsExpanderViewGroup = findViewById(R.id.mainPicker_expandableDay6);
        ViewGroup day6SlotsViewGroup = mTimeSlotsRecyclerView6;
        View day6SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day6SlotsExpanderViewGroup, false);
        day6SlotsExpanderViewGroup.addView(day6SlotsExpandableLayoutView);
        expandableViewGroup6 = new ExpandableViewGroup(dateForApp6, dateForApp6, (ViewGroup) day6SlotsExpandableLayoutView, day6SlotsViewGroup);

        mTimeSlotsRecyclerView7 = findViewById(R.id.mainPicker_timeSlotsList7);
        mTimeSlotsRecyclerView7.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager7 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView7.setLayoutManager(layoutManager7);

        ViewGroup day7SlotsExpanderViewGroup = findViewById(R.id.mainPicker_expandableDay7);
        ViewGroup day7SlotsViewGroup = mTimeSlotsRecyclerView7;
        View day7SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day7SlotsExpanderViewGroup, false);
        day7SlotsExpanderViewGroup.addView(day7SlotsExpandableLayoutView);
        expandableViewGroup7 = new ExpandableViewGroup(dateForApp7, dateForApp7, (ViewGroup) day7SlotsExpandableLayoutView, day7SlotsViewGroup);





        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);
        mSubtitlePage.setTypeface(MLight);
        mEndPage.setTypeface(MMedium);

    }


    public void synchronizeDates() {

        DateFormat dateFormatForFirebase = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormatForApp = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar.getInstance();

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        dateForDB1 = dateFormatForFirebase.format(cal.getTime());
        dateForApp1 = "TODAY";

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        dateForDB2 = dateFormatForFirebase.format(cal.getTime());
        dateForApp2 = "TOMORROW";

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 2);
        dateForDB3 = dateFormatForFirebase.format(cal.getTime());
        dateForApp3 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 3);
        dateForDB4 = dateFormatForFirebase.format(cal.getTime());
        dateForApp4 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 4);
        dateForDB5 = dateFormatForFirebase.format(cal.getTime());
        dateForApp5 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 5);
        dateForDB6 = dateFormatForFirebase.format(cal.getTime());
        dateForApp6 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 6);
        dateForDB7 = dateFormatForFirebase.format(cal.getTime());
        dateForApp7 = dateFormatForApp.format(cal.getTime());

    }


    public void getTrainerFullName()
    {
        final DatabaseReference current_user_db = databaseReference.child("Profile").child("name");
        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mTrainerFullName = dataSnapshot.getValue(String.class);
                mSubtitlePage.setText("With " + mTrainerFullName);
                current_user_db.removeEventListener(this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        traineeTimeSlotsAdapter1 = new TraineeTimeSlotsAdapter(this, databaseReference.child("WeeklySchedule").child(dateForDB1), dateForApp1, dateForDB1, mTraineeId);
        mTimeSlotsRecyclerView1.setAdapter(traineeTimeSlotsAdapter1);

        traineeTimeSlotsAdapter2 = new TraineeTimeSlotsAdapter(this, databaseReference.child("WeeklySchedule").child(dateForDB2), dateForApp2, dateForDB2, mTraineeId);
        mTimeSlotsRecyclerView2.setAdapter(traineeTimeSlotsAdapter2);

        traineeTimeSlotsAdapter3 = new TraineeTimeSlotsAdapter(this, databaseReference.child("WeeklySchedule").child(dateForDB3), dateForApp3, dateForDB3, mTraineeId);
        mTimeSlotsRecyclerView3.setAdapter(traineeTimeSlotsAdapter3);

        traineeTimeSlotsAdapter4 = new TraineeTimeSlotsAdapter(this, databaseReference.child("WeeklySchedule").child(dateForDB4), dateForApp4, dateForDB4, mTraineeId);
        mTimeSlotsRecyclerView4.setAdapter(traineeTimeSlotsAdapter4);

        traineeTimeSlotsAdapter5 = new TraineeTimeSlotsAdapter(this, databaseReference.child("WeeklySchedule").child(dateForDB5), dateForApp5, dateForDB5, mTraineeId);
        mTimeSlotsRecyclerView5.setAdapter(traineeTimeSlotsAdapter5);

        traineeTimeSlotsAdapter6 = new TraineeTimeSlotsAdapter(this, databaseReference.child("WeeklySchedule").child(dateForDB6), dateForApp6, dateForDB6, mTraineeId);
        mTimeSlotsRecyclerView6.setAdapter(traineeTimeSlotsAdapter6);

        traineeTimeSlotsAdapter7 = new TraineeTimeSlotsAdapter(this, databaseReference.child("WeeklySchedule").child(dateForDB7), dateForApp7, dateForDB7, mTraineeId);
        mTimeSlotsRecyclerView7.setAdapter(traineeTimeSlotsAdapter7);

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter.
        traineeTimeSlotsAdapter1.clenup();
        traineeTimeSlotsAdapter2.clenup();
        traineeTimeSlotsAdapter3.clenup();
        traineeTimeSlotsAdapter4.clenup();
        traineeTimeSlotsAdapter5.clenup();
        traineeTimeSlotsAdapter6.clenup();
        traineeTimeSlotsAdapter7.clenup();

    }

}

