package com.gil_shiran_or.keepon.trainee.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gil_shiran_or.keepon.R;

public class StatusFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout weeklyTasksLinearLayout = getView().findViewById(R.id.weekly_tasks);

        getActivity().setTitle("My Status");
        new WeeklyTask(getContext(), weeklyTasksLinearLayout);
    }
}


