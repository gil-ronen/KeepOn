package com.gil_shiran_or.keepon.trainee.my_trainers;

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
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTrainersListAdapter extends RecyclerView.Adapter<MyTrainersListAdapter.MyTrainersViewHolder> {

    private List<MyTrainer> mMyTrainersList = new ArrayList<>();
    private DatabaseReference mDatabaseTraineeTrainersReference;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseTrainersReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers");
    private Fragment mMyTrainersFragment;

    public static class MyTrainersViewHolder extends RecyclerView.ViewHolder {

        public CardView trainerCardView;
        public CircleImageView trainerCircleImageView;
        public TextView trainerNameTextView;

        public MyTrainersViewHolder(View itemView) {
            super(itemView);
            trainerCardView = itemView.findViewById(R.id.my_trainer_item);
            trainerCircleImageView = itemView.findViewById(R.id.my_trainer_profile_img);
            trainerNameTextView = itemView.findViewById(R.id.my_trainer_name);
        }
    }

    public MyTrainersListAdapter(Fragment myTrainersFragment) {
        mMyTrainersFragment = myTrainersFragment;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseTraineeTrainersReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + currentUserId + "/MyTrainers");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMyTrainersList.add(dataSnapshot.getValue(MyTrainer.class));
                notifyItemInserted(mMyTrainersList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < mMyTrainersList.size(); i++) {
                    if (mMyTrainersList.get(i).getUserId().equals(dataSnapshot.child("userId").getValue(String.class))) {
                        mMyTrainersList.remove(i);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeTrainersReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public MyTrainersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trainer_item, parent, false);

        return new MyTrainersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyTrainersViewHolder holder, int position) {
        final MyTrainer currentMyTrainer = mMyTrainersList.get(position);

        holder.trainerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("trainerId", currentMyTrainer.getUserId());
                Intent intent = new Intent(mMyTrainersFragment.getContext(), MyTrainerActivity.class);
                intent.putExtras(bundle);
                mMyTrainersFragment.getContext().startActivity(intent);
            }
        });

        mDatabaseTrainersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String trainerName = dataSnapshot.child(currentMyTrainer.getUserId() + "/Profile/name").getValue(String.class);
                String trainerImageUrl = dataSnapshot.child(currentMyTrainer.getUserId() + "/Profile/profilePhotoUrl").getValue(String.class);

                holder.trainerNameTextView.setText(trainerName);
                Picasso.with(mMyTrainersFragment.getContext()).load(trainerImageUrl).fit().into(holder.trainerCircleImageView);

                mDatabaseTrainersReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMyTrainersList.size();
    }

    public void cleanUp() {
        mDatabaseTraineeTrainersReference.removeEventListener(mChildEventListener);
    }
}
