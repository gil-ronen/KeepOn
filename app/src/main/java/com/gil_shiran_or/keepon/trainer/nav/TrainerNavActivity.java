package com.gil_shiran_or.keepon.trainer.nav;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.login_register.LoginActivity;
import com.gil_shiran_or.keepon.trainee.about.AboutFragment;
import com.gil_shiran_or.keepon.trainer.my_trainees.MyTraineesFragment;
import com.gil_shiran_or.keepon.trainer.profile.ProfileFragment;
import com.gil_shiran_or.keepon.trainer.rating.MyRatingFragment;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.create_and_edit_time_slots.PlanWeeklyScheduleFragment;
import com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.weekly_schedule_view.HomePageWeeklyScheduleFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrainerNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private DatabaseReference mDatabaseTraineesReference;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_nav);

        getWindow().setBackgroundDrawableResource(R.drawable.background_trainee);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomePageWeeklyScheduleFragment()).commit();
            navigationView.setCheckedItem((R.id.nav_main));
        }

        final View header = navigationView.getHeaderView(0);
        String currentUserId = FirebaseAuth.getInstance().getUid();
        mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + currentUserId + "/Profile");
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CircleImageView traineeCircleImageView = header.findViewById(R.id.nav_header_trainee_profile_img);
                TextView traineeNameTextView = header.findViewById(R.id.nav_header_trainee_name);
                TextView traineeEmailTextView = header.findViewById(R.id.nav_header_trainee_email);

                Picasso.with(TrainerNavActivity.this).load(dataSnapshot.child("profilePhotoUrl").getValue(String.class)).fit().into(traineeCircleImageView);
                traineeNameTextView.setText(dataSnapshot.child("name").getValue(String.class));
                traineeEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineesReference.addValueEventListener(mValueEventListener);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomePageWeeklyScheduleFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_my_trainees:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyTraineesFragment()).commit();
                break;
            case R.id.nav_my_rating:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyRatingFragment()).commit();
                break;
            case R.id.nav_schedule_planner:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PlanWeeklyScheduleFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                cleanUp();
                finish();

        }

        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void cleanUp() {
        mDatabaseTraineesReference.removeEventListener(mValueEventListener);
    }
}
