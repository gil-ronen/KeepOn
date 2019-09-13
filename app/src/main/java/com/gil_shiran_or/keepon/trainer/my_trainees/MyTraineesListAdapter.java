package com.gil_shiran_or.keepon.trainer.my_trainees;

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
import com.gil_shiran_or.keepon.trainings_weekly_schedule.trainer_side.weekly_schedule_view.TraineeProfileActivity;
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



public class MyTraineesListAdapter extends RecyclerView.Adapter<MyTraineesListAdapter.MyTraineesViewHolder> {

    private List<MyTrainee> mMyTraineesList = new ArrayList<>();
    private DatabaseReference mDatabaseTraineeTraineesReference;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
    private Fragment mMyTraineesFragment;

    public static class MyTraineesViewHolder extends RecyclerView.ViewHolder {

        public CardView traineeCardView;
        public CircleImageView traineeCircleImageView;
        public TextView traineeNameTextView;

        public MyTraineesViewHolder(View itemView) {
            super(itemView);
            traineeCardView = itemView.findViewById(R.id.my_trainee_item);
            traineeCircleImageView = itemView.findViewById(R.id.my_trainee_profile_img);
            traineeNameTextView = itemView.findViewById(R.id.my_trainee_name);
        }
    }

    public MyTraineesListAdapter(Fragment myTraineesFragment) {
        mMyTraineesFragment = myTraineesFragment;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseTraineeTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + currentUserId + "/MyTrainees");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMyTraineesList.add(dataSnapshot.getValue(MyTrainee.class));
                notifyItemInserted(mMyTraineesList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < mMyTraineesList.size(); i++) {
                    if (mMyTraineesList.get(i).getUserId().equals(dataSnapshot.child("userId").getValue(String.class))) {
                        mMyTraineesList.remove(i);
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

        mDatabaseTraineeTraineesReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public MyTraineesListAdapter.MyTraineesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trainee_item, parent, false);

        return new MyTraineesListAdapter.MyTraineesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyTraineesListAdapter.MyTraineesViewHolder holder, int position) {
        final MyTrainee currentMyTrainee = mMyTraineesList.get(position);


        holder.traineeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent traineeProfile = new Intent(mMyTraineesFragment.getContext() , TraineeProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("traineeId", currentMyTrainee.getUserId());
                traineeProfile.putExtras(bundle);
                mMyTraineesFragment.getContext().startActivity(traineeProfile);
            }
        });



        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String traineeName = dataSnapshot.child(currentMyTrainee.getUserId() + "/Profile/name").getValue(String.class);
                String traineeImageUrl = dataSnapshot.child(currentMyTrainee.getUserId() + "/Profile/profilePhotoUrl").getValue(String.class);

                holder.traineeNameTextView.setText(traineeName);
                Picasso.with(mMyTraineesFragment.getContext()).load(traineeImageUrl).fit().into(holder.traineeCircleImageView);

                mDatabaseTraineesReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMyTraineesList.size();
    }

    public void cleanUp() {
        mDatabaseTraineeTraineesReference.removeEventListener(mChildEventListener);
    }
}
