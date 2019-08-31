package com.gil_shiran_or.keepon.trainee.my_trainers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyTrainerRatingFragment extends Fragment implements AddReviewDialog.AddReviewListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainer_rating, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        createMyTrainerReviewsList();
        buildRecyclerView();

        ViewGroup reviewsExpanderViewGroup = getView().findViewById(R.id.my_trainer_reviews_expander);
        ViewGroup reviewsViewGroup = getView().findViewById(R.id.my_trainer_reviews_list);

        new ExpandableViewGroup("View reviews", "Hide reviews", reviewsExpanderViewGroup, reviewsViewGroup);

        FloatingActionButton addReviewFloatingActionButton = getView().findViewById(R.id.my_trainer_rating_add_review_button);

        final MyTrainerRatingFragment fragment = this;
        addReviewFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReviewDialog addReviewDialog = new AddReviewDialog();

                addReviewDialog.setTargetFragment(fragment, 0);
                addReviewDialog.show(getFragmentManager(), "add review dialog");
            }
        });
    }

    @Override
    public void applyReview(MyTrainerReviewItem myTrainerReviewItem) {
        /*myTrainerReviewsList.add(myTrainerReviewItem);
        myTrainerReviewsListAdapter.notifyItemChanged(myTrainerReviewsList.size() - 1);
        expandableViewGroup.setLabelBefore("View " + myTrainerReviewsList.size() + " reviews");*/
    }

    private void buildReviewsRecyclerView() {
        RecyclerView reviewsRecyclerView = getView().findViewById(R.id.my_trainer_reviews_list);
        reviewsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        MyTrainerReviewsListAdapter myTrainerReviewsListAdapter = new MyTrainerReviewsListAdapter();

        reviewsRecyclerView.setLayoutManager(layoutManager);
        reviewsRecyclerView.setAdapter(myTrainerReviewsListAdapter);
    }
}
