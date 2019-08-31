package com.gil_shiran_or.keepon.trainee.my_trainers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.main.PostsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyTrainersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Trainers");

        buildMyTrainersRecyclerView();
    }

    private void buildMyTrainersRecyclerView() {
        RecyclerView myTrainersRecyclerView = getView().findViewById(R.id.my_trainers_list);
        myTrainersRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        MyTrainersListAdapter myTrainersListAdapter = new MyTrainersListAdapter(this);

        myTrainersRecyclerView.setLayoutManager(layoutManager);
        myTrainersRecyclerView.setAdapter(myTrainersListAdapter);
    }
}
