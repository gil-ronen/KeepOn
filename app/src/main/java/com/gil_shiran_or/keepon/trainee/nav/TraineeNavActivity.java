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

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.login_register.LoginActivity;
import com.gil_shiran_or.keepon.trainee.about.AboutFragment;
import com.gil_shiran_or.keepon.trainee.main.MainFragment;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainersFragment;
import com.gil_shiran_or.keepon.trainee.profile.ProfileFragment;
import com.gil_shiran_or.keepon.trainee.search_trainer.SearchTrainerFragment;
import com.gil_shiran_or.keepon.trainee.settings.SettingsFragment;
import com.gil_shiran_or.keepon.trainee.status.StatusFragment;
import com.google.firebase.auth.FirebaseAuth;

public class TraineeNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_nav);

        getWindow().setBackgroundDrawableResource(R.drawable.background_trainee);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem((R.id.nav_main));
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            case R.id.nav_my_trainers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyTrainersFragment()).commit();
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
                finish();
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
