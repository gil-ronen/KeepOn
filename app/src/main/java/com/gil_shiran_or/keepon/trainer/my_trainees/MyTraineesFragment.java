package com.gil_shiran_or.keepon.trainer.my_trainees;

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

public class MyTraineesFragment extends Fragment {

    private MyTraineesListAdapter mMyTraineesListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainees, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Trainees");

        buildMyTraineesRecyclerView();
        Toast.makeText(getContext(), "Loading trainees...", Toast.LENGTH_SHORT).show();
    }

    private void buildMyTraineesRecyclerView() {
        RecyclerView myTraineesRecyclerView = getView().findViewById(R.id.my_trainees_list);
        myTraineesRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMyTraineesListAdapter = new MyTraineesListAdapter(this);

        myTraineesRecyclerView.setLayoutManager(layoutManager);
        myTraineesRecyclerView.setAdapter(mMyTraineesListAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMyTraineesListAdapter.cleanUp();
    }
}
