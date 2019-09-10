package com.gil_shiran_or.keepon.trainee.search_friend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.search_trainer.TrainersListAdapter;
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendFragment extends Fragment {

    private TraineesListAdapter mTraineesListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_friend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Search Friend");

        buildRecyclerView();
        adjustTrainersSearchView();

        Toast.makeText(getContext(), "Loading Trainees...", Toast.LENGTH_SHORT).show();
    }

    private void buildRecyclerView() {
        RecyclerView trainersRecyclerView = getView().findViewById(R.id.trainees_list);
        trainersRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mTraineesListAdapter = new TraineesListAdapter(this);

        trainersRecyclerView.setLayoutManager(layoutManager);
        trainersRecyclerView.setAdapter(mTraineesListAdapter);
    }

    private void adjustTrainersSearchView() {
        SearchView trainerSearchView = getView().findViewById(R.id.trainer_search);

        trainerSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        trainerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTraineesListAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTraineesListAdapter.cleanUp();
    }
}