package com.gil_shiran_or.keepon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by tutlane on 19-12-2017.
 */

public class TraineeInfoTabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TraineeInfoTabsAdapter(FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                WorkPlanFragment workPlan = new WorkPlanFragment();
                return workPlan;
            case 1:
                PersonalDetailsFragment about = new PersonalDetailsFragment();
                return about;
            default:
                return null;
        }
    }
}