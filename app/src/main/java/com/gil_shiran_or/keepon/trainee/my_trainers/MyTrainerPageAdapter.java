package com.gil_shiran_or.keepon.trainee.my_trainers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyTrainerPageAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;
    private Bundle mBundle;

    MyTrainerPageAdapter(FragmentManager fm, int numOfTabs, Bundle bundle) {
        super(fm);
        mNumOfTabs = numOfTabs;
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyTrainerProfileFragment myTrainerProfileFragment = new MyTrainerProfileFragment();
                myTrainerProfileFragment.setArguments(mBundle);

                return myTrainerProfileFragment;
            case 1:
                MyTrainerRatingFragment myTrainerRatingFragment = new MyTrainerRatingFragment();
                myTrainerRatingFragment.setArguments(mBundle);

                return myTrainerRatingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
