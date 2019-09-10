package com.gil_shiran_or.keepon.trainee.search_friend;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.search_trainer.TrainerPageAdapter;

public class TraineeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee);

        getWindow().setBackgroundDrawableResource(R.drawable.background_trainee);

        Toolbar toolbar = findViewById(R.id.trainee_toolbar);
        toolbar.setTitle("Trainee");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.trainee_tab_layout);
        final ViewPager viewPager = findViewById(R.id.trainee_tab_container);

        TraineePageAdapter pageAdapter = new TraineePageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), getIntent().getExtras());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
}
