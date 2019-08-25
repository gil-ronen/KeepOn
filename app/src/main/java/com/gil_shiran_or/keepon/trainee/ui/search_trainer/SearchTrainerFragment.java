package com.gil_shiran_or.keepon.trainee.ui.search_trainer;

import android.os.Bundle;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.ui.utilities.ExpandableViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SearchTrainerFragment extends Fragment {

    private List<TrainerItem> trainersList;
    private RecyclerView recyclerView;
    private TrainersListAdapter trainersListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ExpandableViewGroup expandableViewGroup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_trainer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Search Trainer");

        createTrainersList();
        buildRecyclerView();
        adjustTrainersPriceRangeSeekBar();
        adjustTrainersRatingRangeSeekBar();
        adjustTrainersSearchView();
        adjustOrderBySpinner();
        adjustFilterOptionsApplyButton();
    }

    private void createTrainersList() {
        trainersList = new ArrayList<>();

        trainersList.add(new TrainerItem("Shiran", "Rishon Le Zion", "Israel", 200, 4.25f, 10));
        trainersList.add(new TrainerItem("Gil", "Haifa", "Israel", 250, 3.5f, 3));
        trainersList.add(new TrainerItem("Or", "Paris", "France", 50, 3.8f, 5));
    }

    private void buildRecyclerView() {
        recyclerView = getView().findViewById(R.id.trainers_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        trainersListAdapter = new TrainersListAdapter(trainersList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(trainersListAdapter);
    }

    private void adjustTrainersPriceRangeSeekBar() {
        CrystalRangeSeekbar trainerPriceRangeSeekBar = getView().findViewById(R.id.trainers_price_range_seek_bar);
        final TextView leftThumbTextView = getView().findViewById(R.id.price_left_thumb_val);
        final TextView rightThumbTextView = getView().findViewById(R.id.price_right_thumb_val);

        trainerPriceRangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                leftThumbTextView.setText(minValue.toString());
                rightThumbTextView.setText(maxValue.toString());
            }
        });
    }

    private void adjustTrainersRatingRangeSeekBar() {
        CrystalRangeSeekbar trainerRatingRangeSeekBar = getView().findViewById(R.id.trainers_rating_range_seek_bar);
        final TextView leftThumbTextView = getView().findViewById(R.id.rating_left_thumb_val);
        final TextView rightThumbTextView = getView().findViewById(R.id.rating_right_thumb_val);

        trainerRatingRangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                leftThumbTextView.setText(minValue.toString());
                rightThumbTextView.setText(maxValue.toString());
            }
        });
    }

    private void adjustTrainersSearchView() {
        SearchView trainerSearchView = getView().findViewById(R.id.trainer_search);
        final ViewGroup filterExpanderLayout = getView().findViewById(R.id.filter_expander);
        final ViewGroup filterOptionsLayout = getView().findViewById(R.id.filter_options);
        expandableViewGroup = new ExpandableViewGroup(getResources().getString(R.string.filter_options_expanding_label),
                getResources().getString(R.string.filter_options_expanding_label), filterExpanderLayout, filterOptionsLayout);

        trainerSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterOptionsLayout.getVisibility() != View.GONE) {
                    expandableViewGroup.collapseAndMakeArrowAnimation();
                }

                filterExpanderLayout.setClickable(false);
                filterExpanderLayout.setFocusable(false);
            }
        });

        trainerSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                filterExpanderLayout.setClickable(true);
                filterExpanderLayout.setFocusable(true);

                return false;
            }
        });

        trainerSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        trainerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                trainersListAdapter.getFilter().filter(newText);

                return false;
            }
        });
    }

    private void adjustOrderBySpinner() {
        Spinner orderBySpinner = getView().findViewById(R.id.order_by_spinner);

        orderBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);

                if (selectedItem.equals(getResources().getString(R.string.order_by_none))) {
                    trainersListAdapter.sortTrainersListByName();
                }
                else if (selectedItem.equals(getResources().getString(R.string.order_by_newest_first))) {

                }
                else if (selectedItem.equals(getResources().getString(R.string.order_by_highest_price))) {
                    trainersListAdapter.sortTrainersListByPrice(false);
                }
                else if (selectedItem.equals(getResources().getString(R.string.order_by_lowest_price))) {
                    trainersListAdapter.sortTrainersListByPrice(true);
                }
                else {
                    trainersListAdapter.sortTrainersListByRating();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void adjustFilterOptionsApplyButton() {
        Button filterOptionsApplyButton = getView().findViewById(R.id.filter_options_apply_button);

        filterOptionsApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner trainersCitiesSpinner = getView().findViewById(R.id.trainers_cities_spinner);
                Spinner trainersGymsSpinner = getView().findViewById(R.id.trainers_gyms_spinner);
                CrystalRangeSeekbar trainersPriceRangeSeekBar = getView().findViewById(R.id.trainers_price_range_seek_bar);
                CrystalRangeSeekbar trainersRatingRangeSeekBar = getView().findViewById(R.id.trainers_rating_range_seek_bar);

                String cityName = trainersCitiesSpinner.getSelectedItem().toString();
                String gymName = trainersGymsSpinner.getSelectedItem().toString();
                int priceMinVal = trainersPriceRangeSeekBar.getSelectedMinValue().intValue();
                int priceMaxVal = trainersPriceRangeSeekBar.getSelectedMaxValue().intValue();
                int ratingMinVal = trainersRatingRangeSeekBar.getSelectedMinValue().intValue();
                int ratingMaxVal = trainersRatingRangeSeekBar.getSelectedMaxValue().intValue();

                if (cityName.equals("None")) {
                    cityName = null;
                }

                if (gymName.equals("None")) {
                    gymName = null;
                }

                if (!trainersListAdapter.filterTrainersListByFilterOptions(cityName, gymName, priceMinVal,
                        priceMaxVal, ratingMinVal, ratingMaxVal)) {
                    Toast.makeText(getContext(), "No Results", Toast.LENGTH_SHORT).show();
                }

                expandableViewGroup.collapseAndMakeArrowAnimation();
            }
        });
    }
}


