package com.gil_shiran_or.keepon.trainer_weekly_planner;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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

    private DatabaseReference databaseReference;


    private TimeSlotsAdapter timeSlotsAdapter1;
    private TimeSlotsAdapter timeSlotsAdapter2;
    private TimeSlotsAdapter timeSlotsAdapter3;
    private TimeSlotsAdapter timeSlotsAdapter4;
    private TimeSlotsAdapter timeSlotsAdapter5;
    private TimeSlotsAdapter timeSlotsAdapter6;
    private TimeSlotsAdapter timeSlotsAdapter7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weekly_schedule);

        mTitlePage = findViewById(R.id.main_titlePage);
        mSubtitlePage = findViewById(R.id.main_subtitlePage);
        mBtnAddNew = findViewById(R.id.main_btnAddNew);
        mEndPage = findViewById(R.id.main_endPage);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("WeeklySchedule");


        mTimeSlotsRecyclerView1 = findViewById(R.id.main_timeSlotsList1);
        mTimeSlotsRecyclerView1.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView1.setLayoutManager(layoutManager1);

        ViewGroup day1SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay1);
        ViewGroup day1SlotsViewGroup = mTimeSlotsRecyclerView1;
        View day1SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day1SlotsExpanderViewGroup, false);
        day1SlotsExpanderViewGroup.addView(day1SlotsExpandableLayoutView);
        expandableViewGroup1 = new ExpandableViewGroup("Day 1", "Day 1", (ViewGroup) day1SlotsExpandableLayoutView, day1SlotsViewGroup);


        mTimeSlotsRecyclerView2 = findViewById(R.id.main_timeSlotsList2);
        mTimeSlotsRecyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView2.setLayoutManager(layoutManager2);

        ViewGroup day2SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay2);
        ViewGroup day2SlotsViewGroup = mTimeSlotsRecyclerView2;
        View day2SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day2SlotsExpanderViewGroup, false);
        day2SlotsExpanderViewGroup.addView(day2SlotsExpandableLayoutView);
        expandableViewGroup2 = new ExpandableViewGroup("Day 2", "Day 2", (ViewGroup) day2SlotsExpandableLayoutView, day2SlotsViewGroup);


        mTimeSlotsRecyclerView3 = findViewById(R.id.main_timeSlotsList3);
        mTimeSlotsRecyclerView3.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView3.setLayoutManager(layoutManager3);

        ViewGroup day3SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay3);
        ViewGroup day3SlotsViewGroup = mTimeSlotsRecyclerView3;
        View day3SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day3SlotsExpanderViewGroup, false);
        day3SlotsExpanderViewGroup.addView(day3SlotsExpandableLayoutView);
        expandableViewGroup3 = new ExpandableViewGroup("Day 3", "Day 3", (ViewGroup) day3SlotsExpandableLayoutView, day3SlotsViewGroup);


        mTimeSlotsRecyclerView4 = findViewById(R.id.main_timeSlotsList4);
        mTimeSlotsRecyclerView4.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView4.setLayoutManager(layoutManager4);

        ViewGroup day4SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay4);
        ViewGroup day4SlotsViewGroup = mTimeSlotsRecyclerView4;
        View day4SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day4SlotsExpanderViewGroup, false);
        day4SlotsExpanderViewGroup.addView(day4SlotsExpandableLayoutView);
        expandableViewGroup4 = new ExpandableViewGroup("Day 4", "Day 4", (ViewGroup) day4SlotsExpandableLayoutView, day4SlotsViewGroup);


        mTimeSlotsRecyclerView5 = findViewById(R.id.main_timeSlotsList5);
        mTimeSlotsRecyclerView5.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView5.setLayoutManager(layoutManager5);

        ViewGroup day5SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay5);
        ViewGroup day5SlotsViewGroup = mTimeSlotsRecyclerView5;
        View day5SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day5SlotsExpanderViewGroup, false);
        day5SlotsExpanderViewGroup.addView(day5SlotsExpandableLayoutView);
        expandableViewGroup5 = new ExpandableViewGroup("Day 5", "Day 5", (ViewGroup) day5SlotsExpandableLayoutView, day5SlotsViewGroup);


        mTimeSlotsRecyclerView6 = findViewById(R.id.main_timeSlotsList6);
        mTimeSlotsRecyclerView6.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView6.setLayoutManager(layoutManager6);

        ViewGroup day6SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay6);
        ViewGroup day6SlotsViewGroup = mTimeSlotsRecyclerView6;
        View day6SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day6SlotsExpanderViewGroup, false);
        day6SlotsExpanderViewGroup.addView(day6SlotsExpandableLayoutView);
        expandableViewGroup6 = new ExpandableViewGroup("Day 6", "Day 6", (ViewGroup) day6SlotsExpandableLayoutView, day6SlotsViewGroup);

        mTimeSlotsRecyclerView7 = findViewById(R.id.main_timeSlotsList7);
        mTimeSlotsRecyclerView7.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager7 = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView7.setLayoutManager(layoutManager7);

        ViewGroup day7SlotsExpanderViewGroup = findViewById(R.id.main_expandableDay7);
        ViewGroup day7SlotsViewGroup = mTimeSlotsRecyclerView7;
        View day7SlotsExpandableLayoutView = getLayoutInflater().inflate(R.layout.expandable_layout, day7SlotsExpanderViewGroup, false);
        day7SlotsExpanderViewGroup.addView(day7SlotsExpandableLayoutView);
        expandableViewGroup7 = new ExpandableViewGroup("Day 7", "Day 7", (ViewGroup) day7SlotsExpandableLayoutView, day7SlotsViewGroup);



        mBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWeeklyScheduleActivity.this, AddNewSlotActivity.class);
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



    @Override
    public void onStart()
    {
        super.onStart();

        timeSlotsAdapter1 = new TimeSlotsAdapter(this, databaseReference.child("day1"), "day1");
        mTimeSlotsRecyclerView1.setAdapter(timeSlotsAdapter1);

        timeSlotsAdapter2 = new TimeSlotsAdapter(this, databaseReference.child("day2"), "day2");
        mTimeSlotsRecyclerView2.setAdapter(timeSlotsAdapter2);

        timeSlotsAdapter3 = new TimeSlotsAdapter(this, databaseReference.child("day3"), "day3");
        mTimeSlotsRecyclerView3.setAdapter(timeSlotsAdapter3);

        timeSlotsAdapter4 = new TimeSlotsAdapter(this, databaseReference.child("day4"), "day4");
        mTimeSlotsRecyclerView4.setAdapter(timeSlotsAdapter4);

        timeSlotsAdapter5 = new TimeSlotsAdapter(this, databaseReference.child("day5"), "day5");
        mTimeSlotsRecyclerView5.setAdapter(timeSlotsAdapter5);

        timeSlotsAdapter6 = new TimeSlotsAdapter(this, databaseReference.child("day6"), "day6");
        mTimeSlotsRecyclerView6.setAdapter(timeSlotsAdapter6);

        timeSlotsAdapter7 = new TimeSlotsAdapter(this, databaseReference.child("day7"), "day7");
        mTimeSlotsRecyclerView7.setAdapter(timeSlotsAdapter7);

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter.
        timeSlotsAdapter1.clenup();
        timeSlotsAdapter2.clenup();
        timeSlotsAdapter3.clenup();
        timeSlotsAdapter4.clenup();
        timeSlotsAdapter5.clenup();
        timeSlotsAdapter6.clenup();
        timeSlotsAdapter7.clenup();

    }

}

