package com.gil_shiran_or.keepon.trainee.ui.my_trainers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyTrainerPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    MyTrainerPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyTrainerProfileFragment();
            case 1:
                return new MyTrainerRatingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
