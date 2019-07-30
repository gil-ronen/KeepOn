package com.gil_shiran_or.keepon.traineer.ratings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gil_shiran_or.keepon.R;

import java.util.ArrayList;
import java.util.List;

public class OnlineRatingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online_ratings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout rootLayout = getView().findViewById(R.id.online_ratings_cardviews);
        String trainerName1 = "Trainer 1";
        String trainerName2 = "Trainer 2";

        new OnlineRatingCard(getContext(), rootLayout, trainerName1);
        new OnlineRatingCard(getContext(), rootLayout, trainerName2);
    }

}
