package com.gil_shiran_or.keepon.trainer_weekly_planner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditSlotActivity extends AppCompatActivity {

    TextView mTitlePage;
    TextView mAddTitle;
    TextView mAddDesc;
    TextView mAddDate;

    EditText mTitleSlot;
    EditText mDescSlot;
    EditText mDateAndTimeSlot;

    Button mBtnSaveUpdate;
    Button mBtnDelete;

    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_slot);

        mTitlePage = findViewById(R.id.edit_titlePage);
        mAddTitle = findViewById(R.id.edit_addTitle);
        mAddDesc = findViewById(R.id.edit_addDesc);
        mAddDate = findViewById(R.id.edit_addDate);

        mTitleSlot = findViewById(R.id.edit_titleSlot);
        mDescSlot = findViewById(R.id.edit_descSlot);
        mDateAndTimeSlot = findViewById(R.id.edit_dateSlot);

        mBtnSaveUpdate = findViewById(R.id.edit_btnSaveUpdate);
        mBtnDelete = findViewById(R.id.edit_btnDelete);

        mTitleSlot.setText(getIntent().getStringExtra("title"));
        mDescSlot.setText(getIntent().getStringExtra("description"));
        mDateAndTimeSlot.setText(getIntent().getStringExtra("dateAndTime"));
        final String mKeySlot = getIntent().getStringExtra("key");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("WeeklySchedule").child(mKeySlot);


        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseReference.removeValue();
                Intent intent = new Intent(EditSlotActivity.this, MainWeeklyScheduleActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mBtnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // update data in database
                String title = mTitleSlot.getText().toString();
                String dateAndTime = mDateAndTimeSlot.getText().toString();
                String description = mDescSlot.getText().toString();

                TimeSlot timeSlot = new TimeSlot(title, dateAndTime, description);

                mDatabaseReference.setValue(timeSlot);

                Intent intent = new Intent(EditSlotActivity.this, MainWeeklyScheduleActivity.class);
                startActivity(intent);
                finish();

            }
        });


        // import font
        Typeface MLight = Typeface.createFromAsset(getAssets(), "fonts/ML.ttf");
        Typeface MMedium = Typeface.createFromAsset(getAssets(), "fonts/MM.ttf");

        // customize font
        mTitlePage.setTypeface(MMedium);

        mAddTitle.setTypeface(MLight);
        mTitleSlot.setTypeface(MMedium);

        mAddDesc.setTypeface(MLight);
        mDescSlot.setTypeface(MMedium);

        mAddDate.setTypeface(MLight);
        mDateAndTimeSlot.setTypeface(MMedium);

        mBtnSaveUpdate.setTypeface(MMedium);
        mBtnDelete.setTypeface(MLight);
    }
}
