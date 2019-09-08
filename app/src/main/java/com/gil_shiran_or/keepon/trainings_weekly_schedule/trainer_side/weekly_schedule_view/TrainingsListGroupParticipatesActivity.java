package com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.weekly_schedule_view;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TrainingsListGroupParticipatesActivity extends AppCompatActivity {

    private TextView mTrainingTitle;
    private TextView mTrainingDesc;
    private TextView mTrainingDate;
    private TextView mTrainingTime;
    private TextView mSumTrainees;
    private TextView mEndPage;

    private RecyclerView mTraineesRecyclerView;


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String mTrainerId;


    private TraineesListAdapter traineesListAdapter;

    private String mDateForDB;
    private String mTimeSlotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainings_list_group_participates);


        mTrainingTitle = findViewById(R.id.trainees_trainingTitle);
        mTrainingDesc = findViewById(R.id.trainees_trainingDesc);
        mTrainingDate = findViewById(R.id.trainees_trainingDate);
        mTrainingTime = findViewById(R.id.trainees_trainingTime);
        mSumTrainees = findViewById(R.id.trainees_counting);
        mEndPage = findViewById(R.id.trainees_endPage);


        mAuth = FirebaseAuth.getInstance();
        mTrainerId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers").child(mTrainerId).child("WeeklySchedule");

        mDateForDB = getIntent().getExtras().getString("dateForDB");
        mTimeSlotId = getIntent().getExtras().getString("slotId");
        mTrainingTitle.setText(getIntent().getExtras().getString("trainingTitle"));
        mTrainingDesc.setText(getIntent().getExtras().getString("trainingDesc"));
        mTrainingDate.setText(getIntent().getExtras().getString("trainingDate"));
        mTrainingTime.setText(getIntent().getExtras().getString("trainingTime"));
        mSumTrainees.setText(getIntent().getExtras().getString("sumTrainees"));



        mTraineesRecyclerView = findViewById(R.id.trainees_traineesList);
        mTraineesRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this.getApplicationContext());
        mTraineesRecyclerView.setLayoutManager(layoutManager1);


        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTrainingTitle.setTypeface(MMedium);
        mTrainingDesc.setTypeface(MLight);
        mTrainingDate.setTypeface(MLight);
        mTrainingTime.setTypeface(MLight);
        mSumTrainees.setTypeface(MLight);
        mEndPage.setTypeface(MMedium);


    }

    @Override
    public void onStart() {
        super.onStart();

        traineesListAdapter = new TraineesListAdapter(this, databaseReference.child(mDateForDB).child(mTimeSlotId).child("traineesId"));
        mTraineesRecyclerView.setAdapter(traineesListAdapter);


    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter.
        traineesListAdapter.clenup();


    }

}