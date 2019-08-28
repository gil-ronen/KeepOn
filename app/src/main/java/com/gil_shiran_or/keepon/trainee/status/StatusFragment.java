package com.gil_shiran_or.keepon.trainee.status;

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

public class StatusFragment extends Fragment {

    private List<WeeklyTaskItem> weeklyTasksList;
    private RecyclerView recyclerView;
    private WeeklyTasksListAdapter weeklyTasksListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Status");

        createWeeklyTasksList();
        buildRecyclerView();
    }

    private void createWeeklyTasksList() {
        weeklyTasksList = new ArrayList<>();

        weeklyTasksList.add(new WeeklyTaskItem("go to the gym 2 times this week", 25));
        weeklyTasksList.add(new WeeklyTaskItem("go to the gym 3 times this week", 50));
    }

    private void buildRecyclerView() {
        recyclerView = getView().findViewById(R.id.weekly_tasks_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        weeklyTasksListAdapter = new WeeklyTasksListAdapter(weeklyTasksList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(weeklyTasksListAdapter);
    }
}


