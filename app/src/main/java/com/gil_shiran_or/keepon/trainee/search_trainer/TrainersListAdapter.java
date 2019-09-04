package com.gil_shiran_or.keepon.trainee.search_trainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.Trainer;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrainersListAdapter extends RecyclerView.Adapter<TrainersListAdapter.TrainersViewHolder> implements Filterable {

    private List<Trainer> mTrainersList = new ArrayList<>();
    private List<Trainer> mFullTrainersList = new ArrayList<>();
    private List<Trainer> optionsFilteredTrainersList = new ArrayList<>();
    private DatabaseReference mDatabaseTrainersReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers");
    private ChildEventListener mChildEventListener;
    private Fragment mSearchTrainerFragment;
    private int mMaxPrice = 0;
    private int mMinPrice = Integer.MAX_VALUE;
    private Filter mFilter;
    private boolean mIsFilterOptionsActive = false;
    private boolean mIsOrderByNameActive = true;
    private boolean mIsOrderByPrice = false;
    private boolean mIsOrderByRating = false;

    public static class TrainersViewHolder extends RecyclerView.ViewHolder {

        public CardView trainerCardView;
        public CircleImageView trainerCircleImageView;
        public TextView trainerNameTextView;
        public TextView trainerGymTextView;
        public TextView trainerCityTextView;
        public TextView trainerPriceTextView;
        public RatingBar trainerRatingBar;
        public TextView trainerRatingScoreTextView;
        public TextView trainerRatingReviewersTextView;

        public TrainersViewHolder(View itemView) {
            super(itemView);
            trainerCardView = itemView.findViewById(R.id.trainer_item);
            trainerCircleImageView = itemView.findViewById(R.id.trainer_profile_img);
            trainerNameTextView = itemView.findViewById(R.id.trainer_name);
            trainerGymTextView = itemView.findViewById(R.id.trainer_gym);
            trainerCityTextView = itemView.findViewById(R.id.trainer_city);
            trainerPriceTextView = itemView.findViewById(R.id.trainer_price);
            trainerRatingBar = itemView.findViewById(R.id.trainer_rating);
            trainerRatingScoreTextView = itemView.findViewById(R.id.trainer_rating_score);
            trainerRatingReviewersTextView = itemView.findViewById(R.id.trainer_rating_reviewers);
        }
    }

    public TrainersListAdapter(Fragment searchTrainerFragment) {
        mSearchTrainerFragment = searchTrainerFragment;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Trainer trainer = dataSnapshot.getValue(Trainer.class);

                trainer.setUserId(dataSnapshot.getKey());
                mFullTrainersList.add(trainer);
                mTrainersList = new ArrayList<>(mFullTrainersList);

                if (Integer.parseInt(trainer.getPrice()) > mMaxPrice) {
                    mMaxPrice = Integer.parseInt(trainer.getPrice());
                }

                if (Integer.parseInt(trainer.getPrice()) < mMinPrice) {
                    mMinPrice = Integer.parseInt(trainer.getPrice());
                }

                ((SearchTrainerFragment) mSearchTrainerFragment).setPriceRange();

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mFullTrainersList.size(); i++) {
                    if (mFullTrainersList.get(i).getUserId().equals(dataSnapshot.getKey())) {
                        Trainer trainer = dataSnapshot.getValue(Trainer.class);

                        trainer.setUserId(dataSnapshot.getKey());
                        mFullTrainersList.set(i, trainer);
                        mTrainersList = new ArrayList<>(mFullTrainersList);
                        break;
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTrainersReference.addChildEventListener(mChildEventListener);

        mFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Trainer> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    if (optionsFilteredTrainersList.isEmpty()) {
                        filteredList.addAll(mFullTrainersList);
                    }
                    else {
                        filteredList.addAll(optionsFilteredTrainersList);
                    }
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (Trainer trainer : mFullTrainersList) {
                        if (trainer.getUsername().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(trainer);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTrainersList.clear();
                mTrainersList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public int getMaxPrice() {
        return mMaxPrice;
    }

    public int getMinPrice() {
        return mMinPrice;
    }

    @Override
    public TrainersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_item, parent, false);

        return new TrainersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrainersViewHolder holder, int position) {
        final Trainer currentTrainer = mTrainersList.get(position);

        holder.trainerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("trainerId", currentTrainer.getUserId());
                Intent intent = new Intent(mSearchTrainerFragment.getContext(), TrainerActivity.class);
                intent.putExtras(bundle);
                mSearchTrainerFragment.getContext().startActivity(intent);
            }
        });

        Picasso.with(mSearchTrainerFragment.getContext()).load(currentTrainer.getProfilePhotoUri()).fit().into(holder.trainerCircleImageView);
        holder.trainerNameTextView.setText(currentTrainer.getUsername());
        holder.trainerGymTextView.setText(currentTrainer.getCompanyName());
        holder.trainerCityTextView.setText(currentTrainer.getTrainingPlaceAddress());
        holder.trainerPriceTextView.setText(currentTrainer.getPrice() + "\u20aa");

        int reviewersNum = currentTrainer.getRating().getOne_star_reviewers_num() + currentTrainer.getRating().getTwo_stars_reviewers_num() +
                currentTrainer.getRating().getThree_stars_reviewers_num() + currentTrainer.getRating().getFour_stars_reviewers_num() +
                currentTrainer.getRating().getFive_stars_reviewers_num();
        float ratingScore;

        if (reviewersNum == 0) {
            ratingScore = 0;
        }
        else {
            ratingScore = currentTrainer.getRating().getSum_rates() / reviewersNum;
        }

        holder.trainerRatingBar.setRating(ratingScore);
        holder.trainerRatingScoreTextView.setText(String.format("%,.2f", ratingScore));
        holder.trainerRatingReviewersTextView.setText(" (" + reviewersNum + ")");
    }

    @Override
    public int getItemCount() {
        return mTrainersList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void sortTrainersListByName() {
        Collections.sort(mFullTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return trainer1.getUsername().compareTo(trainer2.getUsername());
            }
        });

        Collections.sort(mTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return trainer1.getUsername().compareTo(trainer2.getUsername());
            }
        });

        notifyDataSetChanged();
    }

    public void sortTrainersListByPrice(boolean isAscending) {
        Collections.sort(mFullTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return Integer.compare(Integer.parseInt(trainer1.getPrice()), Integer.parseInt(trainer2.getPrice()));
            }
        });

        Collections.sort(mTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return Integer.compare(Integer.parseInt(trainer1.getPrice()), Integer.parseInt(trainer2.getPrice()));
            }
        });


        if (!isAscending) {
            Collections.reverse(mFullTrainersList);
            Collections.reverse(mTrainersList);
        }

        notifyDataSetChanged();
    }

    public void sortTrainersListByRating() {
        Collections.sort(mFullTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                int reviewersNum1 = trainer1.getRating().getOne_star_reviewers_num() + trainer1.getRating().getTwo_stars_reviewers_num() +
                        trainer1.getRating().getThree_stars_reviewers_num() + trainer1.getRating().getFour_stars_reviewers_num() +
                        trainer1.getRating().getFive_stars_reviewers_num();
                float ratingScore1;

                if (reviewersNum1 == 0) {
                    ratingScore1 = 0;
                }
                else {
                    ratingScore1 = trainer1.getRating().getSum_rates() / reviewersNum1;
                }

                int reviewersNum2 = trainer2.getRating().getOne_star_reviewers_num() + trainer2.getRating().getTwo_stars_reviewers_num() +
                        trainer2.getRating().getThree_stars_reviewers_num() + trainer2.getRating().getFour_stars_reviewers_num() +
                        trainer2.getRating().getFive_stars_reviewers_num();
                float ratingScore2;

                if (reviewersNum2 == 0) {
                    ratingScore2 = 0;
                }
                else {
                    ratingScore2 = trainer1.getRating().getSum_rates() / reviewersNum1;
                }

                return Float.compare(ratingScore1, ratingScore2);
            }
        });

        Collections.sort(mTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                int reviewersNum1 = trainer1.getRating().getOne_star_reviewers_num() + trainer1.getRating().getTwo_stars_reviewers_num() +
                        trainer1.getRating().getThree_stars_reviewers_num() + trainer1.getRating().getFour_stars_reviewers_num() +
                        trainer1.getRating().getFive_stars_reviewers_num();
                float ratingScore1;

                if (reviewersNum1 == 0) {
                    ratingScore1 = 0;
                }
                else {
                    ratingScore1 = trainer1.getRating().getSum_rates() / reviewersNum1;
                }

                int reviewersNum2 = trainer2.getRating().getOne_star_reviewers_num() + trainer2.getRating().getTwo_stars_reviewers_num() +
                        trainer2.getRating().getThree_stars_reviewers_num() + trainer2.getRating().getFour_stars_reviewers_num() +
                        trainer2.getRating().getFive_stars_reviewers_num();
                float ratingScore2;

                if (reviewersNum2 == 0) {
                    ratingScore2 = 0;
                }
                else {
                    ratingScore2 = trainer1.getRating().getSum_rates() / reviewersNum1;
                }


                return Float.compare(ratingScore1, ratingScore2);
            }
        });

        Collections.reverse(mFullTrainersList);
        Collections.reverse(mTrainersList);
        notifyDataSetChanged();
    }

    public boolean filterTrainersListByFilterOptions(String cityName, String gymName, int priceMinVal,
                                                  int priceMaxVal, int ratingMinVal, int ratingMaxVal) {
        optionsFilteredTrainersList.clear();

        for (Trainer trainer : mFullTrainersList) {
            if (cityName != null) {
                if (!trainer.getTrainingPlaceAddress().equals(cityName)) {
                    continue;
                }
            }

            if (gymName != null) {
                if (!trainer.getCompanyName().equals(gymName)) {
                    continue;
                }
            }

            if (Integer.parseInt(trainer.getPrice()) < priceMinVal || Integer.parseInt(trainer.getPrice()) > priceMaxVal) {
                continue;
            }

            int reviewersNum = trainer.getRating().getOne_star_reviewers_num() + trainer.getRating().getTwo_stars_reviewers_num() +
                    trainer.getRating().getThree_stars_reviewers_num() + trainer.getRating().getFour_stars_reviewers_num() +
                    trainer.getRating().getFive_stars_reviewers_num();
            float ratingScore;

            if (reviewersNum == 0) {
                ratingScore = 0;
            }
            else {
                ratingScore = trainer.getRating().getSum_rates() / reviewersNum;
            }

            if (ratingScore < ratingMinVal || ratingScore > ratingMaxVal) {
                continue;
            }

            optionsFilteredTrainersList.add(trainer);
        }

        if (optionsFilteredTrainersList.isEmpty()) {
            return false;
        }

        mTrainersList.clear();
        mTrainersList.addAll(optionsFilteredTrainersList);
        notifyDataSetChanged();

        return true;
    }
}
