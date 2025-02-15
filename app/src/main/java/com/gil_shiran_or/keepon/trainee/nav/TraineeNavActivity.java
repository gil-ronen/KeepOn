package com.gil_shiran_or.keepon.trainee.nav;

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
import com.gil_shiran_or.keepon.trainee.main.MainFragment;
import com.gil_shiran_or.keepon.trainee.my_friends.MyFriendsFragment;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainersFragment;
import com.gil_shiran_or.keepon.trainee.profile.ProfileFragment;
import com.gil_shiran_or.keepon.trainee.search_friend.SearchFriendFragment;
import com.gil_shiran_or.keepon.trainee.search_trainer.SearchTrainerFragment;
import com.gil_shiran_or.keepon.trainee.settings.SettingsFragment;
import com.gil_shiran_or.keepon.trainee.status.StatusFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TraineeNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private DatabaseReference mDatabaseTraineeReference;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_nav);

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
                    new MainFragment()).commit();
            navigationView.setCheckedItem((R.id.nav_main));
        }

        final View header = navigationView.getHeaderView(0);
        String currentUserId = FirebaseAuth.getInstance().getUid();
        mDatabaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + currentUserId + "/Profile");
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CircleImageView traineeCircleImageView = header.findViewById(R.id.nav_header_trainee_profile_img);
                TextView traineeNameTextView = header.findViewById(R.id.nav_header_trainee_name);
                TextView traineeEmailTextView = header.findViewById(R.id.nav_header_trainee_email);

                Picasso.with(TraineeNavActivity.this).load(dataSnapshot.child("profilePhotoUrl").getValue(String.class)).fit().into(traineeCircleImageView);
                traineeNameTextView.setText(dataSnapshot.child("name").getValue(String.class));
                traineeEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeReference.addValueEventListener(mValueEventListener);
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
                        new MainFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_status:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new StatusFragment()).commit();
                break;
            case R.id.nav_search_trainer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchTrainerFragment()).commit();
                break;
            case R.id.nav_search_friend:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFriendFragment()).commit();
                break;
            case R.id.nav_my_trainers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyTrainersFragment()).commit();
                break;
            case R.id.nav_my_friends:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyFriendsFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
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
        mDatabaseTraineeReference.removeEventListener(mValueEventListener);
    }
}
