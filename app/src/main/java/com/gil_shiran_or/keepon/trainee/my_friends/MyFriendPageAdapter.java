package com.gil_shiran_or.keepon.trainee.my_friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerProfileFragment;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerRatingFragment;

public class MyFriendPageAdapter extends FragmentPagerAdapter {

    private int mNumOfTabs;
    private Bundle mBundle;

    MyFriendPageAdapter(FragmentManager fm, int numOfTabs, Bundle bundle) {
        super(fm);
        mNumOfTabs = numOfTabs;
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MyFriendProfileFragment myFriendProfileFragment = new MyFriendProfileFragment();
                myFriendProfileFragment.setArguments(mBundle);

                return myFriendProfileFragment;
            case 1:
                MyFriendStatusFragment myFriendStatusFragment = new MyFriendStatusFragment();
                myFriendStatusFragment.setArguments(mBundle);

                return myFriendStatusFragment;
            case 2:
                MyFriendTrainersFragment myFriendTrainersFragment = new MyFriendTrainersFragment();
                myFriendTrainersFragment.setArguments(mBundle);

                return myFriendTrainersFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
