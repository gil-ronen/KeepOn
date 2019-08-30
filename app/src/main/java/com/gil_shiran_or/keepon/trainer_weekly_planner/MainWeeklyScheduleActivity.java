package com.gil_shiran_or.keepon.trainer_weekly_planner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

