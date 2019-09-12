package com.gil_shiran_or.keepon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TraineeProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseTraineeReference;
    private ValueEventListener mValueEventListener;
    private String mTraineeId;
    private String mTraineeGender;
    private String mTraineeProfilePhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_profile);

        getWindow().setBackgroundDrawableResource(R.drawable.background_trainee);

        mTraineeId = getIntent().getExtras().getString("traineeId");

        Toolbar toolbar = findViewById(R.id.trainee_toolbar);
        toolbar.setTitle("Trainee Profile");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final CircleImageView traineeCircleImageView = findViewById(R.id.trainee_profile_img);
        final TextView traineeNameTextView = findViewById(R.id.trainee_name);
        final TextView traineeEmailTextView = findViewById(R.id.trainee_email);
        final TextView traineeCityTextView = findViewById(R.id.trainee_city);
        final TextView traineeStreetTextView = findViewById(R.id.trainee_street);
        final TextView traineeBirthDateTextView = findViewById(R.id.trainee_birth_date);
        final TextView traineePhoneNumberTextView = findViewById(R.id.trainee_phone_number);
        final ImageView traineeGenderImageView = findViewById(R.id.trainee_gender);

        mDatabaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/Profile");
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTraineeProfilePhotoUrl = dataSnapshot.child("profilePhotoUrl").getValue(String.class);
                Picasso.with(TraineeProfileActivity.this).load(mTraineeProfilePhotoUrl).fit().into(traineeCircleImageView);
                traineeNameTextView.setText(dataSnapshot.child("name").getValue(String.class));
                traineeEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
                traineeCityTextView.setText(dataSnapshot.child("city").getValue(String.class));
                traineeStreetTextView.setText(dataSnapshot.child("street").getValue(String.class));
                traineeBirthDateTextView.setText(dataSnapshot.child("birthDate").getValue(String.class));
                traineePhoneNumberTextView.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                mTraineeGender = dataSnapshot.child("gender").getValue(String.class);

                if (mTraineeGender.equals("male")) {
                    traineeGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_male_sign));
                }
                else {
                    traineeGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_female_sign));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeReference.addValueEventListener(mValueEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTraineeReference.removeEventListener(mValueEventListener);
    }
}
