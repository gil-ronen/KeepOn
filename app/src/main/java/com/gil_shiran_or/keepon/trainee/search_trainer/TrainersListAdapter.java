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
import com.gil_shiran_or.keepon.db.Rating;
import com.gil_shiran_or.keepon.db.Trainer;
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
                Trainer trainer = dataSnapshot.child("Profile").getValue(Trainer.class);
                Rating rating = dataSnapshot.child("Rating").getValue(Rating.class);

                trainer.setUserId(dataSnapshot.getKey());
                trainer.setRating(rating);
                mFullTrainersList.add(trainer);
                mTrainersList = new ArrayList<>(mFullTrainersList);

                if (trainer.getPrice() > mMaxPrice) {
                    mMaxPrice = trainer.getPrice();
                }

                if (trainer.getPrice() < mMinPrice) {
                    mMinPrice = trainer.getPrice();
                }

                ((SearchTrainerFragment) mSearchTrainerFragment).setPriceRange();

                notifyItemInserted(mTrainersList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mFullTrainersList.size(); i++) {
                    if (mFullTrainersList.get(i).getUserId().equals(dataSnapshot.getKey())) {
                        Trainer trainer = dataSnapshot.getValue(Trainer.class);

                        trainer.setUserId(dataSnapshot.getKey());
                        mFullTrainersList.set(i, trainer);
                        mTrainersList = new ArrayList<>(mFullTrainersList);
                        notifyItemChanged(i);
                        break;
                    }
                }
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
                        if (trainer.getName().toLowerCase().startsWith(filterPattern)) {
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

        Picasso.with(mSearchTrainerFragment.getContext()).load(currentTrainer.getProfilePhotoUrl()).fit().into(holder.trainerCircleImageView);
        holder.trainerNameTextView.setText(currentTrainer.getName());
        holder.trainerGymTextView.setText(currentTrainer.getCompanyName());
        holder.trainerCityTextView.setText(currentTrainer.getTrainingCity());
        holder.trainerPriceTextView.setText(currentTrainer.getPrice() + "\u20aa");

        holder.trainerRatingBar.setRating(currentTrainer.getRating().getRating());
        holder.trainerRatingScoreTextView.setText(String.format("%,.2f", currentTrainer.getRating().getRating()));
        holder.trainerRatingReviewersTextView.setText(" (" + currentTrainer.getRating().getTotalRaters() + ")");
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
                return trainer1.getName().compareTo(trainer2.getName());
            }
        });

        Collections.sort(mTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return trainer1.getName().compareTo(trainer2.getName());
            }
        });

        notifyDataSetChanged();
    }

    public void sortTrainersListByPrice(boolean isAscending) {
        Collections.sort(mFullTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return Integer.compare(trainer1.getPrice(), trainer2.getPrice());
            }
        });

        Collections.sort(mTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return Integer.compare(trainer1.getPrice(), trainer2.getPrice());
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
                return Float.compare(trainer1.getRating().getRating(), trainer2.getRating().getRating());
            }
        });

        Collections.sort(mTrainersList, new Comparator<Trainer>() {
            @Override
            public int compare(Trainer trainer1, Trainer trainer2) {
                return Float.compare(trainer1.getRating().getRating(), trainer2.getRating().getRating());
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
                if (!trainer.getTrainingCity().equals(cityName)) {
                    continue;
                }
            }

            if (gymName != null) {
                if (!trainer.getCompanyName().equals(gymName)) {
                    continue;
                }
            }

            if (trainer.getPrice() < priceMinVal || trainer.getPrice() > priceMaxVal) {
                continue;
            }

            if (trainer.getRating().getRating() < ratingMinVal || trainer.getRating().getRating() > ratingMaxVal) {
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

    public void cleanUp() {
        mDatabaseTrainersReference.removeEventListener(mChildEventListener);
    }
}
