package com.gil_shiran_or.keepon.trainer_weekly_planner.trainer_side;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainWeeklyScheduleActivity extends AppCompatActivity {

    private TextView mTitlePage;
    private TextView mSubtitlePage;
    private TextView mEndPage;
    private Button mBtnAddNew;

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


    private TrainerTimeSlotsAdapter trainerTimeSlotsAdapter1;
    private TrainerTimeSlotsAdapter trainerTimeSlotsAdapter2;
    private TrainerTimeSlotsAdapter trainerTimeSlotsAdapter3;
    private TrainerTimeSlotsAdapter trainerTimeSlotsAdapter4;
    private TrainerTimeSlotsAdapter trainerTimeSlotsAdapter5;
    private TrainerTimeSlotsAdapter trainerTimeSlotsAdapter6;
    private TrainerTimeSlotsAdapter trainerTimeSlotsAdapter7;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weekly_schedule);

        mTitlePage = findViewById(R.id.main_titlePage);
        mSubtitlePage = findViewById(R.id.main_subtitlePage);
        mBtnAddNew = findViewById(R.id.main_btnAddNew);
        mEndPage = findViewById(R.id.main_endPage);

        //TODO: TRAINER ID NEED TO TAKEN FROM CURRENT USER FROM DB!!!
        mTrainerId = "ayAWQUYKUZbISD7FicSJvYOWShE3";
        //mAuth = FirebaseAuth.getInstance();
        //String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(mTrainerId).child("WeeklySchedule");
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child("1EnOxPPh0cez6CzKnypPXvSZ1052").child("WeeklySchedule");

        synchronizeDates();

        mTimeSlotsRecyclerView1 = findViewById(R.id.main_timeSlotsList1);
        mTimeSlotsRecyclerView1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView1.setLayoutManager(layoutManager1);

        ViewGroup day1SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay1);
        ViewGroup day1SlotsViewGroup = mTimeSlotsRecyclerView1;
        View day1SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day1SlotsExpanderViewGroup, false);
        day1SlotsExpanderViewGroup.addView(day1SlotsExpandableLayoutView);
        expandableViewGroup1 = new ExpandableViewGroup(dateForApp1, dateForApp1, (ViewGroup) day1SlotsExpandableLayoutView, day1SlotsViewGroup);


        mTimeSlotsRecyclerView2 = findViewById(R.id.main_timeSlotsList2);
        mTimeSlotsRecyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView2.setLayoutManager(layoutManager2);

        ViewGroup day2SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay2);
        ViewGroup day2SlotsViewGroup = mTimeSlotsRecyclerView2;
        View day2SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day2SlotsExpanderViewGroup, false);
        day2SlotsExpanderViewGroup.addView(day2SlotsExpandableLayoutView);
        expandableViewGroup2 = new ExpandableViewGroup(dateForApp2, dateForApp2, (ViewGroup) day2SlotsExpandableLayoutView, day2SlotsViewGroup);


        mTimeSlotsRecyclerView3 = findViewById(R.id.main_timeSlotsList3);
        mTimeSlotsRecyclerView3.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView3.setLayoutManager(layoutManager3);

        ViewGroup day3SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay3);
        ViewGroup day3SlotsViewGroup = mTimeSlotsRecyclerView3;
        View day3SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day3SlotsExpanderViewGroup, false);
        day3SlotsExpanderViewGroup.addView(day3SlotsExpandableLayoutView);
        expandableViewGroup3 = new ExpandableViewGroup(dateForApp3, dateForApp3, (ViewGroup) day3SlotsExpandableLayoutView, day3SlotsViewGroup);


        mTimeSlotsRecyclerView4 = findViewById(R.id.main_timeSlotsList4);
        mTimeSlotsRecyclerView4.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView4.setLayoutManager(layoutManager4);

        ViewGroup day4SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay4);
        ViewGroup day4SlotsViewGroup = mTimeSlotsRecyclerView4;
        View day4SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day4SlotsExpanderViewGroup, false);
        day4SlotsExpanderViewGroup.addView(day4SlotsExpandableLayoutView);
        expandableViewGroup4 = new ExpandableViewGroup(dateForApp4, dateForApp4, (ViewGroup) day4SlotsExpandableLayoutView, day4SlotsViewGroup);


        mTimeSlotsRecyclerView5 = findViewById(R.id.main_timeSlotsList5);
        mTimeSlotsRecyclerView5.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView5.setLayoutManager(layoutManager5);

        ViewGroup day5SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay5);
        ViewGroup day5SlotsViewGroup = mTimeSlotsRecyclerView5;
        View day5SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day5SlotsExpanderViewGroup, false);
        day5SlotsExpanderViewGroup.addView(day5SlotsExpandableLayoutView);
        expandableViewGroup5 = new ExpandableViewGroup(dateForApp5, dateForApp5, (ViewGroup) day5SlotsExpandableLayoutView, day5SlotsViewGroup);


        mTimeSlotsRecyclerView6 = findViewById(R.id.main_timeSlotsList6);
        mTimeSlotsRecyclerView6.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView6.setLayoutManager(layoutManager6);

        ViewGroup day6SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay6);
        ViewGroup day6SlotsViewGroup = mTimeSlotsRecyclerView6;
        View day6SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day6SlotsExpanderViewGroup, false);
        day6SlotsExpanderViewGroup.addView(day6SlotsExpandableLayoutView);
        expandableViewGroup6 = new ExpandableViewGroup(dateForApp6, dateForApp6, (ViewGroup) day6SlotsExpandableLayoutView, day6SlotsViewGroup);

        mTimeSlotsRecyclerView7 = findViewById(R.id.main_timeSlotsList7);
        mTimeSlotsRecyclerView7.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager7 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView7.setLayoutManager(layoutManager7);

        ViewGroup day7SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay7);
        ViewGroup day7SlotsViewGroup = mTimeSlotsRecyclerView7;
        View day7SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day7SlotsExpanderViewGroup, false);
        day7SlotsExpanderViewGroup.addView(day7SlotsExpandableLayoutView);
        expandableViewGroup7 = new ExpandableViewGroup(dateForApp7, dateForApp7, (ViewGroup) day7SlotsExpandableLayoutView, day7SlotsViewGroup);


        mBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainWeeklyScheduleActivity.this, AddNewSlotActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("dateForDB1", dateForDB1);
                bundle.putString("dateForApp1", dateForApp1);
                bundle.putString("dateForDB2", dateForDB2);
                bundle.putString("dateForApp2", dateForApp2);
                bundle.putString("dateForDB3", dateForDB3);
                bundle.putString("dateForApp3", dateForApp3);
                bundle.putString("dateForDB4", dateForDB4);
                bundle.putString("dateForApp4", dateForApp4);
                bundle.putString("dateForDB5", dateForDB5);
                bundle.putString("dateForApp5", dateForApp5);
                bundle.putString("dateForDB6", dateForDB6);
                bundle.putString("dateForApp6", dateForApp6);
                bundle.putString("dateForDB7", dateForDB7);
                bundle.putString("dateForApp7", dateForApp7);

                intent.putExtras(bundle);
                startActivity(intent);
                //finish();
            }
        });


        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);
        mSubtitlePage.setTypeface(MLight);
        mEndPage.setTypeface(MMedium);
        mBtnAddNew.setTypeface(MLight);

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
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1 );
        dateForDB2 = dateFormatForFirebase.format(cal.getTime());
        dateForApp2 = "TOMORROW";

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 2 );
        dateForDB3 = dateFormatForFirebase.format(cal.getTime());
        dateForApp3 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 3 );
        dateForDB4 = dateFormatForFirebase.format(cal.getTime());
        dateForApp4 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 4 );
        dateForDB5 = dateFormatForFirebase.format(cal.getTime());
        dateForApp5 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 5 );
        dateForDB6 = dateFormatForFirebase.format(cal.getTime());
        dateForApp6 = dateFormatForApp.format(cal.getTime());

        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 6 );
        dateForDB7 = dateFormatForFirebase.format(cal.getTime());
        dateForApp7 = dateFormatForApp.format(cal.getTime());

    }


    @Override
    public void onStart() {
        super.onStart();

        trainerTimeSlotsAdapter1 = new TrainerTimeSlotsAdapter(this, databaseReference.child(dateForDB1), dateForApp1, dateForDB1);
        mTimeSlotsRecyclerView1.setAdapter(trainerTimeSlotsAdapter1);

        trainerTimeSlotsAdapter2 = new TrainerTimeSlotsAdapter(this, databaseReference.child(dateForDB2), dateForApp2, dateForDB2);
        mTimeSlotsRecyclerView2.setAdapter(trainerTimeSlotsAdapter2);

        trainerTimeSlotsAdapter3 = new TrainerTimeSlotsAdapter(this, databaseReference.child(dateForDB3), dateForApp3, dateForDB3);
        mTimeSlotsRecyclerView3.setAdapter(trainerTimeSlotsAdapter3);

        trainerTimeSlotsAdapter4 = new TrainerTimeSlotsAdapter(this, databaseReference.child(dateForDB4), dateForApp4, dateForDB4);
        mTimeSlotsRecyclerView4.setAdapter(trainerTimeSlotsAdapter4);

        trainerTimeSlotsAdapter5 = new TrainerTimeSlotsAdapter(this, databaseReference.child(dateForDB5), dateForApp5, dateForDB5);
        mTimeSlotsRecyclerView5.setAdapter(trainerTimeSlotsAdapter5);

        trainerTimeSlotsAdapter6 = new TrainerTimeSlotsAdapter(this, databaseReference.child(dateForDB6), dateForApp6 ,dateForDB6);
        mTimeSlotsRecyclerView6.setAdapter(trainerTimeSlotsAdapter6);

        trainerTimeSlotsAdapter7 = new TrainerTimeSlotsAdapter(this, databaseReference.child(dateForDB7), dateForApp7, dateForDB7);
        mTimeSlotsRecyclerView7.setAdapter(trainerTimeSlotsAdapter7);

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter.
        trainerTimeSlotsAdapter1.clenup();
        trainerTimeSlotsAdapter2.clenup();
        trainerTimeSlotsAdapter3.clenup();
        trainerTimeSlotsAdapter4.clenup();
        trainerTimeSlotsAdapter5.clenup();
        trainerTimeSlotsAdapter6.clenup();
        trainerTimeSlotsAdapter7.clenup();

    }

}

