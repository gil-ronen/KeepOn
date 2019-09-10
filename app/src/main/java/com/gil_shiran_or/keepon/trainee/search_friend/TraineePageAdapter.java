package com.gil_shiran_or.keepon.trainee.search_friend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gil_shiran_or.keepon.trainee.search_trainer.TrainerProfileFragment;
import com.gil_shiran_or.keepon.trainee.search_trainer.TrainerRatingFragment;

public class TraineePageAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;
    private Bundle mBundle;

    TraineePageAdapter(FragmentManager fm, int numOfTabs, Bundle bundle) {
        super(fm);
        mNumOfTabs = numOfTabs;
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TraineeProfileFragment traineeProfileFragment = new TraineeProfileFragment();
                traineeProfileFragment.setArguments(mBundle);

                return traineeProfileFragment;
            case 1:
                TraineeStatusFragment traineeStatusFragment = new TraineeStatusFragment();
                traineeStatusFragment.setArguments(mBundle);

                return traineeStatusFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
