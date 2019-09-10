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

public class MyFriendsFragment extends Fragment {

    private MyFriendsListAdapter mMyFriendsListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Friends");
        buildMyTrainersRecyclerView();

        Toast.makeText(getContext(), "Loading friends...", Toast.LENGTH_SHORT).show();
    }

    private void buildMyTrainersRecyclerView() {
        RecyclerView myTrainersRecyclerView = getView().findViewById(R.id.my_friends_list);
        myTrainersRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMyFriendsListAdapter = new MyFriendsListAdapter(this);

        myTrainersRecyclerView.setLayoutManager(layoutManager);
        myTrainersRecyclerView.setAdapter(mMyFriendsListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyFriendsListAdapter.cleanUp();
    }
}
