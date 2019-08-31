package com.gil_shiran_or.keepon.trainer_weekly_planner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
    private ExpandableViewGroup expandableViewGroup;
    private DatabaseReference databaseReference;
    private ListView slotsListView1;
    private ListView slotsListView2;
    private ListView slotsListView3;
    private ListView slotsListView4;
    private ListView slotsListView5;
    private ListView slotsListView6;
    private ListView slotsListView7;
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
        slotsListView1 = findViewById(R.id.main_timeSlotsListView1);
        slotsListView2 = findViewById(R.id.main_timeSlotsListView2);
        slotsListView3 = findViewById(R.id.main_timeSlotsListView3);
        slotsListView4 = findViewById(R.id.main_timeSlotsListView4);
        slotsListView5 = findViewById(R.id.main_timeSlotsListView5);
        slotsListView6 = findViewById(R.id.main_timeSlotsListView6);
        slotsListView7 = findViewById(R.id.main_timeSlotsListView7);


        ViewGroup reviewsExpanderViewGroup1 = findViewById(R.id.main_expandableDay1);
        ViewGroup reviewsViewGroup1 = slotsListView1;
        View expandableLayoutView1 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup1, false);
        reviewsExpanderViewGroup1.addView(expandableLayoutView1);
        expandableViewGroup = new ExpandableViewGroup("Day 1", "Day 1", (ViewGroup) expandableLayoutView1, reviewsViewGroup1);

        ViewGroup reviewsExpanderViewGroup2 = findViewById(R.id.main_expandableDay2);
        ViewGroup reviewsViewGroup2 = slotsListView2;
        View expandableLayoutView2 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup2, false);
        reviewsExpanderViewGroup2.addView(expandableLayoutView2);
        expandableViewGroup = new ExpandableViewGroup("Day 2 ", "Day 2 ", (ViewGroup) expandableLayoutView2, reviewsViewGroup2);

        ViewGroup reviewsExpanderViewGroup3 = findViewById(R.id.main_expandableDay3);
        ViewGroup reviewsViewGroup3 = slotsListView3;
        View expandableLayoutView3 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup3, false);
        reviewsExpanderViewGroup3.addView(expandableLayoutView3);
        expandableViewGroup = new ExpandableViewGroup("Day 3 ", "Day 3 ", (ViewGroup) expandableLayoutView3, reviewsViewGroup3);

        ViewGroup reviewsExpanderViewGroup4 = findViewById(R.id.main_expandableDay4);
        ViewGroup reviewsViewGroup4 = slotsListView4;
        View expandableLayoutView4 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup4, false);
        reviewsExpanderViewGroup4.addView(expandableLayoutView4);
        expandableViewGroup = new ExpandableViewGroup("Day 4 ", "Day 4 ", (ViewGroup) expandableLayoutView4, reviewsViewGroup4);

        ViewGroup reviewsExpanderViewGroup5 = findViewById(R.id.main_expandableDay5);
        ViewGroup reviewsViewGroup5 = slotsListView5;
        View expandableLayoutView5 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup5, false);
        reviewsExpanderViewGroup5.addView(expandableLayoutView5);
        expandableViewGroup = new ExpandableViewGroup("Day 5 ", "Day 5 ", (ViewGroup) expandableLayoutView5, reviewsViewGroup5);

        ViewGroup reviewsExpanderViewGroup6 = findViewById(R.id.main_expandableDay6);
        ViewGroup reviewsViewGroup6 = slotsListView6;
        View expandableLayoutView6 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup6, false);
        reviewsExpanderViewGroup6.addView(expandableLayoutView6);
        expandableViewGroup = new ExpandableViewGroup("Day 6 ", "Day 6 ", (ViewGroup) expandableLayoutView6, reviewsViewGroup6);

        ViewGroup reviewsExpanderViewGroup7 = findViewById(R.id.main_expandableDay7);
        ViewGroup reviewsViewGroup7 = slotsListView7;
        View expandableLayoutView7 = getLayoutInflater().inflate(R.layout.expandable_layout, reviewsExpanderViewGroup7, false);
        reviewsExpanderViewGroup7.addView(expandableLayoutView7);
        expandableViewGroup = new ExpandableViewGroup("Day 7 ", "Day 7 ", (ViewGroup) expandableLayoutView7, reviewsViewGroup7);





        mBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWeeklyScheduleActivity.this, AddNewSlotActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        slotsListView1.setOnItemClickListener(onItemClickListener);
        slotsListView2.setOnItemClickListener(onItemClickListener);
        slotsListView3.setOnItemClickListener(onItemClickListener);
        slotsListView4.setOnItemClickListener(onItemClickListener);
        slotsListView5.setOnItemClickListener(onItemClickListener);
        slotsListView6.setOnItemClickListener(onItemClickListener);
        slotsListView7.setOnItemClickListener(onItemClickListener);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);
        mSubtitlePage.setTypeface(MLight);
        mEndPage.setTypeface(MMedium);
        mBtnAddNew.setTypeface(MLight);

    }

    AdapterView.OnItemClickListener onItemClickListener  = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            TimeSlot item = (TimeSlot) adapterView.getItemAtPosition(i);

            final String getTitleSlot = item.getTitle();
            final String getDescSlot = item.getDescription();
            final String getDateSlot = item.getDateAndTime();
            final String getIdSlot = item.getTimeSlotId();

            Intent intent = new Intent(MainWeeklyScheduleActivity.this, EditSlotActivity.class);
            intent.putExtra("title", getTitleSlot);
            intent.putExtra("description", getDescSlot);
            intent.putExtra("dateAndTime", getDateSlot);
            intent.putExtra("key", getIdSlot);
            startActivity(intent);
            //finish();
        }
    };


    @Override
    public void onStart()
    {
        super.onStart();
        timeSlotsAdapter1 = new TimeSlotsAdapter(this, databaseReference);
        slotsListView1.setAdapter(timeSlotsAdapter1);

        timeSlotsAdapter2 = new TimeSlotsAdapter(this, databaseReference);
        slotsListView2.setAdapter(timeSlotsAdapter2);

        timeSlotsAdapter3 = new TimeSlotsAdapter(this, databaseReference);
        slotsListView3.setAdapter(timeSlotsAdapter3);

        timeSlotsAdapter4 = new TimeSlotsAdapter(this, databaseReference);
        slotsListView4.setAdapter(timeSlotsAdapter4);

        timeSlotsAdapter5 = new TimeSlotsAdapter(this, databaseReference);
        slotsListView5.setAdapter(timeSlotsAdapter5);

        timeSlotsAdapter6 = new TimeSlotsAdapter(this, databaseReference);
        slotsListView6.setAdapter(timeSlotsAdapter6);

        timeSlotsAdapter7 = new TimeSlotsAdapter(this, databaseReference);
        slotsListView7.setAdapter(timeSlotsAdapter7);
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

