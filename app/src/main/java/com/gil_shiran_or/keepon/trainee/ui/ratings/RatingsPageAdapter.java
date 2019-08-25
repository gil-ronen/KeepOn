package com.gil_shiran_or.keepon.trainee.ui.ratings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RatingsPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    RatingsPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyRatingsFragment();
            case 1:
                return new OnlineRatingsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
