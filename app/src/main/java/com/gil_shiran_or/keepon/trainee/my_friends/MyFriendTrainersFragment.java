package com.gil_shiran_or.keepon.trainee.my_friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainersListAdapter;

public class MyFriendTrainersFragment extends Fragment {

    private MyFriendTrainersListAdapter mMyFriendTrainersListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        buildMyTrainersRecyclerView();
    }

    private void buildMyTrainersRecyclerView() {
        RecyclerView myFriendTrainersRecyclerView = getView().findViewById(R.id.my_trainers_list);
        myFriendTrainersRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMyFriendTrainersListAdapter = new MyFriendTrainersListAdapter(this);

        myFriendTrainersRecyclerView.setLayoutManager(layoutManager);
        myFriendTrainersRecyclerView.setAdapter(mMyFriendTrainersListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyFriendTrainersListAdapter.cleanUp();
    }
}
