package com.gil_shiran_or.keepon.trainee.my_friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerProfileFragment;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerRatingFragment;

public class MyFriendTrainerPageAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;
    private Bundle mBundle;

    MyFriendTrainerPageAdapter(FragmentManager fm, int numOfTabs, Bundle bundle) {
        super(fm);
        mNumOfTabs = numOfTabs;
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyFriendTrainerProfileFragment myFriendTrainerProfileFragment = new MyFriendTrainerProfileFragment();
                myFriendTrainerProfileFragment.setArguments(mBundle);

                return myFriendTrainerProfileFragment;
            case 1:
                MyFriendTrainerRatingFragment myFriendTrainerRatingFragment = new MyFriendTrainerRatingFragment();
                myFriendTrainerRatingFragment.setArguments(mBundle);

                return myFriendTrainerRatingFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
