package com.gil_shiran_or.keepon.trainee.search_trainer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrainersListAdapter extends RecyclerView.Adapter<TrainersListAdapter.TrainersViewHolder> implements Filterable {

    private List<TrainerItem> trainersList;
    private List<TrainerItem> fullTrainersList;
    private List<TrainerItem> optionsFilteredTrainersList = new ArrayList<>();
    private Filter filter;

    public static class TrainersViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView trainerCircleImageView;
        public TextView trainerNameTextView;
        public TextView trainerCityTextView;
        public TextView trainerGymTextView;
        public TextView trainerPriceTextView;
        public RatingBar ratingBar;
        public TextView trainerRatingNumTextView;
        public TextView trainerReviewersNumTextView;

        public TrainersViewHolder(View itemView) {
            super(itemView);
            trainerCircleImageView = itemView.findViewById(R.id.trainer_item_trainer_profile_img);
            trainerNameTextView = itemView.findViewById(R.id.trainer_item_trainer_name);
            trainerCityTextView = itemView.findViewById(R.id.trainer_item_trainer_city);
            trainerGymTextView = itemView.findViewById(R.id.trainer_item_trainer_country);
            trainerPriceTextView = itemView.findViewById(R.id.trainer_item_trainer_price);
            ratingBar = itemView.findViewById(R.id.trainer_item_trainer_rating);
            trainerRatingNumTextView = itemView.findViewById(R.id.trainer_item_trainer_rating_num);
            trainerReviewersNumTextView = itemView.findViewById(R.id.trainer_item_trainer_reviewers_num);
        }
    }

    public TrainersListAdapter(final List<TrainerItem> trainersList) {
        this.trainersList = trainersList;
        fullTrainersList = new ArrayList<>(this.trainersList);
        filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<TrainerItem> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    if (optionsFilteredTrainersList.isEmpty()) {
                        filteredList.addAll(fullTrainersList);
                    }
                    else {
                        filteredList.addAll(optionsFilteredTrainersList);
                    }
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (TrainerItem trainerItem : fullTrainersList) {
                        if (trainerItem.getTrainerName().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(trainerItem);
                        } else if (trainerItem.getTrainerCity().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(trainerItem);
                        } else if (filterPattern.compareTo(Float.toString(trainerItem.getTrainerRating())) <= 0) {
                            filteredList.add(trainerItem);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                trainersList.clear();
                trainersList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public TrainersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_item, parent, false);

        return new TrainersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrainersViewHolder holder, int position) {
        TrainerItem currentTrainerItem = trainersList.get(position);

        holder.trainerNameTextView.setText(currentTrainerItem.getTrainerName());
        holder.trainerCityTextView.setText(currentTrainerItem.getTrainerCity());
        holder.trainerGymTextView.setText(currentTrainerItem.getTrainerGym());
        holder.trainerPriceTextView.setText(Integer.toString(currentTrainerItem.getTrainerPrice()) + "\u20aa");
        holder.ratingBar.setRating(currentTrainerItem.getTrainerRating());
        holder.trainerRatingNumTextView.setText(Float.toString(currentTrainerItem.getTrainerRating()));
        holder.trainerReviewersNumTextView.setText(" (" + currentTrainerItem.getTrainerNumOfReviewers() + ")");
    }

    @Override
    public int getItemCount() {
        return trainersList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void sortTrainersListByName() {
        Collections.sort(fullTrainersList, new Comparator<TrainerItem>() {
            @Override
            public int compare(TrainerItem trainerItem1, TrainerItem trainerItem2) {
                return trainerItem1.getTrainerName().compareTo(trainerItem2.getTrainerName());
            }
        });

        Collections.sort(trainersList, new Comparator<TrainerItem>() {
            @Override
            public int compare(TrainerItem trainerItem1, TrainerItem trainerItem2) {
                return trainerItem1.getTrainerName().compareTo(trainerItem2.getTrainerName());
            }
        });

        notifyDataSetChanged();
    }

    public void sortTrainersListByPrice(boolean isAscending) {
        Collections.sort(fullTrainersList, new Comparator<TrainerItem>() {
            @Override
            public int compare(TrainerItem trainerItem1, TrainerItem trainerItem2) {
                return Integer.compare(trainerItem1.getTrainerPrice(), trainerItem2.getTrainerPrice());
            }
        });

        Collections.sort(trainersList, new Comparator<TrainerItem>() {
            @Override
            public int compare(TrainerItem trainerItem1, TrainerItem trainerItem2) {
                return Integer.compare(trainerItem1.getTrainerPrice(), trainerItem2.getTrainerPrice());
            }
        });


        if (!isAscending) {
            Collections.reverse(fullTrainersList);
            Collections.reverse(trainersList);
        }

        notifyDataSetChanged();
    }

    public void sortTrainersListByRating() {
        Collections.sort(fullTrainersList, new Comparator<TrainerItem>() {
            @Override
            public int compare(TrainerItem trainerItem1, TrainerItem trainerItem2) {
                return Float.compare(trainerItem1.getTrainerRating(), trainerItem2.getTrainerRating());
            }
        });

        Collections.sort(trainersList, new Comparator<TrainerItem>() {
            @Override
            public int compare(TrainerItem trainerItem1, TrainerItem trainerItem2) {
                return Float.compare(trainerItem1.getTrainerRating(), trainerItem2.getTrainerRating());
            }
        });

        Collections.reverse(fullTrainersList);
        Collections.reverse(trainersList);
        notifyDataSetChanged();
    }

    public boolean filterTrainersListByFilterOptions(String cityName, String gymName, int priceMinVal,
                                                  int priceMaxVal, int ratingMinVal, int ratingMaxVal) {
        optionsFilteredTrainersList.clear();

        for (TrainerItem trainerItem : fullTrainersList) {
            if (cityName != null) {
                if (!trainerItem.getTrainerCity().equals(cityName)) {
                    continue;
                }
            }

            if (gymName != null) {
                if (!trainerItem.getTrainerGym().equals(gymName)) {
                    continue;
                }
            }

            if (trainerItem.getTrainerPrice() < priceMinVal || trainerItem.getTrainerPrice() > priceMaxVal) {
                continue;
            }

            if (trainerItem.getTrainerRating() < ratingMinVal || trainerItem.getTrainerRating() > ratingMaxVal) {
                continue;
            }

            optionsFilteredTrainersList.add(trainerItem);
        }

        if (optionsFilteredTrainersList.isEmpty()) {
            return false;
        }

        trainersList.clear();
        trainersList.addAll(optionsFilteredTrainersList);
        notifyDataSetChanged();

        return true;
    }
}
