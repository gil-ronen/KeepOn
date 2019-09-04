package com.gil_shiran_or.keepon.trainee.search_trainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerProfileFragment;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerRatingFragment;

public class TrainerPageAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;
    private Bundle mBundle;

    TrainerPageAdapter(FragmentManager fm, int numOfTabs, Bundle bundle) {
        super(fm);
        mNumOfTabs = numOfTabs;
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TrainerProfileFragment trainerProfileFragment = new TrainerProfileFragment();
                trainerProfileFragment.setArguments(mBundle);

                return trainerProfileFragment;
            case 1:
                TrainerRatingFragment trainerRatingFragment = new TrainerRatingFragment();
                trainerRatingFragment.setArguments(mBundle);

                return trainerRatingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
