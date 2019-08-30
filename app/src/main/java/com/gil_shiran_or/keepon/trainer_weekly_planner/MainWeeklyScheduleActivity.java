package com.gil_shiran_or.keepon.trainer_weekly_planner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainWeeklyScheduleActivity extends AppCompatActivity {

    private TextView mTitlePage;
    private TextView mSubtitlePage;
    //private TextView mEndPage;
    private Button mBtnAddNew;

    private DatabaseReference databaseReference;
    private ListView slotsListView;
    private TimeSlotsAdapter timeSlotsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_weekly_schedule);

        mTitlePage = findViewById(R.id.main_titlePage);
        mSubtitlePage = findViewById(R.id.main_subtitlePage);
        //mEndPage = findViewById(R.id.main_endPage);
        mBtnAddNew = findViewById(R.id.main_btnAddNew);
        slotsListView = findViewById(R.id.main_timeSlotsListView);

        mBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWeeklyScheduleActivity.this, AddNewSlotActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        slotsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);
        mSubtitlePage.setTypeface(MLight);
        //mEndPage.setTypeface(MLight);
        mBtnAddNew.setTypeface(MLight);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        timeSlotsAdapter = new TimeSlotsAdapter(this, databaseReference);
        slotsListView.setAdapter(timeSlotsAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter.
        timeSlotsAdapter.clenup();
    }

}

