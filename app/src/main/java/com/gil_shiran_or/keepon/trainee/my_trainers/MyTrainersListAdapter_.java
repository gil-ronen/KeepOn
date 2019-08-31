package com.gil_shiran_or.keepon.trainee.my_trainers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class MyTrainersListAdapter_ extends BaseAdapter {

    private DatabaseReference mDatabaseMyTrainersReference;
    private DatabaseReference mDatabaseTrainersReference;
    private List<DataSnapshot> mMyTrainersList;
    private String mCurrentUserId;
    private Fragment mMyTrainersFragment;

    public MyTrainersListAdapter_(Fragment myTrainersFragment) {
        mDatabaseMyTrainersReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/myTrainers");
        mDatabaseTrainersReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers");
        mMyTrainersList = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();
        mMyTrainersFragment = myTrainersFragment;

        mDatabaseMyTrainersReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMyTrainersList.add(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        });
    }

    private static class ViewHolder
    {
        public CircleImageView trainerCircleImageView;
        public TextView trainerNameTextView;
    }

    @Override
    public int getCount() {
        return mMyTrainersList.size();
    }

    @Override
    public MyTrainer getItem(int position) {
        DataSnapshot dataSnapshot = mMyTrainersList.get(position);
        MyTrainer myTrainer = dataSnapshot.getValue(MyTrainer.class);

        return myTrainer;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trainer_item, parent, false);

            ViewHolder holder = new ViewHolder();

            holder.trainerCircleImageView = convertView.findViewById(R.id.my_trainer_profile_img);
            holder.trainerNameTextView = convertView.findViewById(R.id.my_trainer_name);

            convertView.setTag(holder);
        }

        final MyTrainer myTrainer = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        mDatabaseTrainersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String myTrainerName = dataSnapshot.child(myTrainer.getUserId() + "/username").getValue(String.class);
                String authorImageUrl = dataSnapshot.child(myTrainer.getUserId() + "/profilePhotoUri").getValue(String.class);

                holder.trainerNameTextView.setText(myTrainerName);
                Picasso.with(mMyTrainersFragment.getContext()).load(authorImageUrl).fit().into(holder.trainerCircleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return convertView;
    }
}
