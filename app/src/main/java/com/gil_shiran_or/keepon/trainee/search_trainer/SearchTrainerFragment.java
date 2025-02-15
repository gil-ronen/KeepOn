package com.gil_shiran_or.keepon.trainee.search_trainer;

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
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchTrainerFragment extends Fragment {

    private TrainersListAdapter mTrainersListAdapter;
    private ExpandableViewGroup mExpandableViewGroup;
    private DatabaseReference mDatabaseTrainersReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers");
    private ValueEventListener mValueEventListener;
    private List<String> mSpinnerCities = new ArrayList<>();
    private List<String> mSpinnerCompanies = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_trainer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Search Trainer");

        buildRecyclerView();
        adjustTrainersPriceRangeSeekBar();
        adjustTrainersRatingRangeSeekBar();
        adjustTrainersSearchView();
        adjustOrderBySpinner();
        adjustSpinners();
        adjustFilterOptionsApplyButton();

        Toast.makeText(getContext(), "Loading Trainers...", Toast.LENGTH_SHORT).show();
    }

    private void buildRecyclerView() {
        RecyclerView trainersRecyclerView = getView().findViewById(R.id.trainers_list);
        trainersRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mTrainersListAdapter = new TrainersListAdapter(this);

        trainersRecyclerView.setLayoutManager(layoutManager);
        trainersRecyclerView.setAdapter(mTrainersListAdapter);
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
        mExpandableViewGroup = new ExpandableViewGroup(getResources().getString(R.string.filter_options_expanding_label),
                getResources().getString(R.string.filter_options_expanding_label), filterExpanderLayout, filterOptionsLayout);

        trainerSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterOptionsLayout.getVisibility() != View.GONE) {
                    mExpandableViewGroup.collapseAndMakeArrowAnimation();
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
                mTrainersListAdapter.getFilter().filter(newText);

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
                    mTrainersListAdapter.sortTrainersListByName();
                }
                else if (selectedItem.equals(getResources().getString(R.string.order_by_highest_price))) {
                    mTrainersListAdapter.sortTrainersListByPrice(false);
                }
                else if (selectedItem.equals(getResources().getString(R.string.order_by_lowest_price))) {
                    mTrainersListAdapter.sortTrainersListByPrice(true);
                }
                else {
                    mTrainersListAdapter.sortTrainersListByRating();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setPriceRange() {
        CrystalRangeSeekbar trainersPriceRangeSeekBar = getView().findViewById(R.id.trainers_price_range_seek_bar);
        TextView maxPriceTextView = getView().findViewById(R.id.price_right_thumb_val);
        TextView minPriceTextView = getView().findViewById(R.id.price_left_thumb_val);

        trainersPriceRangeSeekBar.setMaxValue(mTrainersListAdapter.getMaxPrice());
        trainersPriceRangeSeekBar.setMinValue(mTrainersListAdapter.getMinPrice());
        trainersPriceRangeSeekBar.setMaxStartValue(mTrainersListAdapter.getMaxPrice());
        trainersPriceRangeSeekBar.setMinStartValue(mTrainersListAdapter.getMinPrice());

        maxPriceTextView.setText(Integer.toString(mTrainersListAdapter.getMaxPrice()));
        minPriceTextView.setText(Integer.toString(mTrainersListAdapter.getMinPrice()));
    }

    private void adjustSpinners() {
        mSpinnerCities.add("None");
        mSpinnerCompanies.add("None");

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String city = data.child("Profile/trainingCity").getValue(String.class);
                    String company = data.child("Profile/companyName").getValue(String.class);

                    if (!mSpinnerCities.contains(city)) {
                        mSpinnerCities.add(city);
                    }

                    if (!mSpinnerCompanies.contains(company)) {
                        mSpinnerCompanies.add(company);
                    }
                }

                setSpinnersAdapters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTrainersReference.addValueEventListener(mValueEventListener);
    }

    private void setSpinnersAdapters() {
        Spinner trainersCitiesSpinner = getView().findViewById(R.id.trainers_cities_spinner);
        Spinner trainersCompaniesSpinner = getView().findViewById(R.id.trainers_companies_spinner);
        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mSpinnerCities);
        ArrayAdapter<String> companiesAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, mSpinnerCompanies);

        trainersCitiesSpinner.setAdapter(citiesAdapter);
        trainersCompaniesSpinner.setAdapter(companiesAdapter);
    }

    private void adjustFilterOptionsApplyButton() {
        Button filterOptionsApplyButton = getView().findViewById(R.id.filter_options_apply_button);

        filterOptionsApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner trainersCitiesSpinner = getView().findViewById(R.id.trainers_cities_spinner);
                Spinner trainersCompaniesSpinner = getView().findViewById(R.id.trainers_companies_spinner);
                CrystalRangeSeekbar trainersPriceRangeSeekBar = getView().findViewById(R.id.trainers_price_range_seek_bar);
                CrystalRangeSeekbar trainersRatingRangeSeekBar = getView().findViewById(R.id.trainers_rating_range_seek_bar);

                String cityName = trainersCitiesSpinner.getSelectedItem().toString();
                String companyName = trainersCompaniesSpinner.getSelectedItem().toString();
                int priceMinVal = trainersPriceRangeSeekBar.getSelectedMinValue().intValue();
                int priceMaxVal = trainersPriceRangeSeekBar.getSelectedMaxValue().intValue();
                int ratingMinVal = trainersRatingRangeSeekBar.getSelectedMinValue().intValue();
                int ratingMaxVal = trainersRatingRangeSeekBar.getSelectedMaxValue().intValue();

                if (cityName.equals("None")) {
                    cityName = null;
                }

                if (companyName.equals("None")) {
                    companyName = null;
                }

                if (!mTrainersListAdapter.filterTrainersListByFilterOptions(cityName, companyName, priceMinVal,
                        priceMaxVal, ratingMinVal, ratingMaxVal)) {
                    Toast.makeText(getContext(), "No Results", Toast.LENGTH_SHORT).show();
                }

                mExpandableViewGroup.collapseAndMakeArrowAnimation();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTrainersReference.removeEventListener(mValueEventListener);
    }
}