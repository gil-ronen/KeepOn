package com.gil_shiran_or.keepon.login_register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.nav.TraineeNavActivity;
import com.gil_shiran_or.keepon.trainer.nav.TrainerNavActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LandingScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Query isTraineeQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if(mUser == null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            String user_id = mUser.getUid();

            isTraineeQuery = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainees")
                    .orderByKey()
                    .equalTo(user_id);
            isTraineeQuery.addValueEventListener(isTraineeValueEventListener);

        }

    }

    ValueEventListener isTraineeValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Intent intent;

            if (dataSnapshot.exists()) //the user is connected as Trainee
            {
                intent = new Intent(getApplicationContext(), TraineeNavActivity.class);
            }
            else //the user is connected as Trainer
            {
                intent = new Intent(getApplicationContext(), TrainerNavActivity.class);
            }

            isTraineeQuery.removeEventListener(this);
            startActivity(intent);
            finish();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };
}
