package com.gil_shiran_or.keepon.trainee.ratings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gil_shiran_or.keepon.R;

import java.util.ArrayList;
import java.util.List;

public class MyRatingsFragment extends Fragment {

    private List<MyRatingsItem> myRatingsList;
    private RecyclerView recyclerView;
    private MyRatingsListAdapter myRatingsListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_ratings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        createMyRatingsList();
        buildRecyclerView();
    }

    private void createMyRatingsList() {
        myRatingsList = new ArrayList<>();

        myRatingsList.add(new MyRatingsItem("Shiran"));
        myRatingsList.add(new MyRatingsItem("Gil"));
        myRatingsList.add(new MyRatingsItem("Or"));
    }

    private void buildRecyclerView() {
        recyclerView = getView().findViewById(R.id.my_ratings_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        myRatingsListAdapter = new MyRatingsListAdapter(myRatingsList, getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myRatingsListAdapter);
    }
}
