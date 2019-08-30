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

    private List<MyTrainerItem> myTrainersList;
    private RecyclerView recyclerView;
    private MyTrainersListAdapter myTrainersListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Trainers");

        buildMyTrainersListView();

        /*createMyTrainersList();
        buildRecyclerView();*/
    }

    private void createMyTrainersList() {
        myTrainersList = new ArrayList<>();

        myTrainersList.add(new MyTrainerItem("Shiran"));
        myTrainersList.add(new MyTrainerItem("Gil"));
        myTrainersList.add(new MyTrainerItem("Or"));
    }

    private void buildMyTrainersListView() {
        ListView myTrainersListView = getView().findViewById(R.id.my_trainers_list);
        MyTrainersListAdapter_ myTrainersListAdapter = new MyTrainersListAdapter_(this);

        myTrainersListView.setAdapter(myTrainersListAdapter);
    }

    private void buildRecyclerView() {
        recyclerView = getView().findViewById(R.id.my_trainers_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        myTrainersListAdapter = new MyTrainersListAdapter(myTrainersList, getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myTrainersListAdapter);
    }
}
