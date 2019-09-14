package com.gil_shiran_or.keepon.trainee.search_friend;

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
import com.gil_shiran_or.keepon.db.Status;
import com.gil_shiran_or.keepon.db.Trainee;
import com.gil_shiran_or.keepon.db.Trainer;
import com.gil_shiran_or.keepon.trainee.search_trainer.SearchTrainerFragment;
import com.gil_shiran_or.keepon.trainee.search_trainer.TrainerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TraineesListAdapter extends RecyclerView.Adapter<TraineesListAdapter.TrainersViewHolder> implements Filterable {

    private List<Trainee> mTraineesList = new ArrayList<>();
    private List<Trainee> mFullTraineesList = new ArrayList<>();
    private DatabaseReference mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
    private Fragment mSearchTraineeFragment;
    private Filter mFilter;
    private String mCurrentUserId;

    public static class TrainersViewHolder extends RecyclerView.ViewHolder {

        public CardView traineeCardView;
        public CircleImageView traineeCircleImageView;
        public TextView traineeNameTextView;
        public TextView traineeCityTextView;
        public TextView traineeEmailTextView;

        public TrainersViewHolder(View itemView) {
            super(itemView);
            traineeCardView = itemView.findViewById(R.id.trainee_item);
            traineeCircleImageView = itemView.findViewById(R.id.trainee_profile_img);
            traineeNameTextView = itemView.findViewById(R.id.trainee_name);
            traineeCityTextView = itemView.findViewById(R.id.trainee_city);
            traineeEmailTextView = itemView.findViewById(R.id.trainee_email);
        }
    }

    public TraineesListAdapter(Fragment searchTraineeFragment) {
        mSearchTraineeFragment = searchTraineeFragment;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (!data.getKey().equals(mCurrentUserId)) {
                        Trainee trainee = data.child("Profile").getValue(Trainee.class);
                        Status status = data.child("Status").getValue(Status.class);

                        trainee.setUserId(data.getKey());
                        trainee.setStatus(status);
                        mFullTraineesList.add(trainee);
                        mTraineesList = new ArrayList<>(mFullTraineesList);

                        notifyItemInserted(mTraineesList.size() - 1);
                    }
                }

                mDatabaseTraineesReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Trainee> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(mFullTraineesList);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

                    for (Trainee trainee : mFullTraineesList) {
                        if (trainee.getName().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(trainee);
                        }
                        else if (trainee.getEmail().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(trainee);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTraineesList.clear();
                mTraineesList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public TrainersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainee_item, parent, false);

        return new TrainersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrainersViewHolder holder, int position) {
        final Trainee currentTrainee = mTraineesList.get(position);

        holder.traineeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("traineeId", currentTrainee.getUserId());
                Intent intent = new Intent(mSearchTraineeFragment.getContext(), TraineeActivity.class);
                intent.putExtras(bundle);
                mSearchTraineeFragment.getContext().startActivity(intent);
            }
        });

        Picasso.with(mSearchTraineeFragment.getContext()).load(currentTrainee.getProfilePhotoUrl()).fit().into(holder.traineeCircleImageView);
        holder.traineeNameTextView.setText(currentTrainee.getName());
        holder.traineeCityTextView.setText(currentTrainee.getCity());
        holder.traineeEmailTextView.setText(currentTrainee.getEmail());
    }

    @Override
    public int getItemCount() {
        return mTraineesList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}
