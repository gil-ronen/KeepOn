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
    private RecyclerView mTimeSlotsRecyclerView;

    private ExpandableViewGroup expandableViewGroup;
    private DatabaseReference databaseReference;


    private TimeSlotsAdapter timeSlotsAdapter1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weekly_schedule);

        mTitlePage = findViewById(R.id.main_titlePage);
        mSubtitlePage = findViewById(R.id.main_subtitlePage);
        mBtnAddNew = findViewById(R.id.main_btnAddNew);
        mEndPage = findViewById(R.id.main_endPage);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        //buildTimeSlotsRecyclerView();
        mTimeSlotsRecyclerView = findViewById(R.id.main_timeSlotsList1);
        mTimeSlotsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext());
        mTimeSlotsRecyclerView.setLayoutManager(layoutManager);




        ViewGroup reviewsExpanderViewGroup1 = findViewById(R.id.main_expandableDay1);
        ViewGroup reviewsViewGroup1 = mTimeSlotsRecyclerView;
        View expandableLayoutView1 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup1, false);
        reviewsExpanderViewGroup1.addView(expandableLayoutView1);
        expandableViewGroup = new ExpandableViewGroup("Day 1", "Day 1", (ViewGroup) expandableLayoutView1, reviewsViewGroup1);



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
        timeSlotsAdapter1 = new TimeSlotsAdapter(this, databaseReference);
        mTimeSlotsRecyclerView.setAdapter(timeSlotsAdapter1);


    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter.
        timeSlotsAdapter1.clenup();

    }

}

