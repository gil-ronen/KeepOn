package com.gil_shiran_or.keepon.trainee.my_friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainer;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerActivity;
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

public class MyFriendsListAdapter extends RecyclerView.Adapter<MyFriendsListAdapter.MyTrainersViewHolder> {

    private List<MyFriend> mMyFriendsList = new ArrayList<>();
    private DatabaseReference mDatabaseTraineeFriendsReference;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
    private Fragment mMyFriendsFragment;
    private String mCurrentUserId;

    public static class MyTrainersViewHolder extends RecyclerView.ViewHolder {

        public CardView traineeCardView;
        public CircleImageView traineeCircleImageView;
        public TextView traineeNameTextView;

        public MyTrainersViewHolder(View itemView) {
            super(itemView);
            traineeCardView = itemView.findViewById(R.id.my_friend_item);
            traineeCircleImageView = itemView.findViewById(R.id.my_friend_profile_img);
            traineeNameTextView = itemView.findViewById(R.id.my_friend_name);
        }
    }

    public MyFriendsListAdapter(Fragment myFriendsFragment) {
        mMyFriendsFragment = myFriendsFragment;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseTraineeFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyFriends");
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMyFriendsList.add(dataSnapshot.getValue(MyFriend.class));
                notifyItemInserted(mMyFriendsList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mMyFriendsList.size(); i++) {
                    if (mMyFriendsList.get(i).getUserId().equals(dataSnapshot.child("userId").getValue(String.class))) {
                        MyFriend myFriend = dataSnapshot.getValue(MyFriend.class);

                        mMyFriendsList.set(i, myFriend);
                        notifyItemChanged(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < mMyFriendsList.size(); i++) {
                    if (mMyFriendsList.get(i).getUserId().equals(dataSnapshot.child("userId").getValue(String.class))) {
                        mMyFriendsList.remove(i);
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

        mDatabaseTraineeFriendsReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public MyTrainersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_friend_item, parent, false);

        return new MyTrainersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyTrainersViewHolder holder, int position) {
        final MyFriend currentMyFriend = mMyFriendsList.get(position);

        if (!currentMyFriend.getIsAccepted()) {
            holder.traineeCardView.setCardBackgroundColor(mMyFriendsFragment.getResources().getColor(R.color.disabled));

            if (!currentMyFriend.getIsSendRequest()) {
                holder.traineeCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(mMyFriendsFragment.getContext())
                                .setTitle("Accept Trainee")
                                .setMessage("Are you sure you want to accept this trainee to become your friend?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final DatabaseReference databaseMyFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyFriends");
                                        final DatabaseReference databaseTraineeFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + currentMyFriend.getUserId() + "/MyFriends");

                                        databaseMyFriendsReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    MyFriend myFriend = data.getValue(MyFriend.class);
                                                    if (myFriend.getUserId().equals(currentMyFriend.getUserId())) {
                                                        databaseMyFriendsReference.child(data.getKey() + "/isAccepted").setValue(true);
                                                        break;
                                                    }
                                                }

                                                databaseMyFriendsReference.removeEventListener(this);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        databaseTraineeFriendsReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    MyFriend myFriend = data.getValue(MyFriend.class);
                                                    if (myFriend.getUserId().equals(mCurrentUserId)) {
                                                        databaseTraineeFriendsReference.child(data.getKey() + "/isAccepted").setValue(true);
                                                        break;
                                                    }
                                                }

                                                databaseTraineeFriendsReference.removeEventListener(this);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final DatabaseReference databaseMyFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyFriends");
                                        final DatabaseReference databaseTraineeFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + currentMyFriend.getUserId() + "/MyFriends");

                                        databaseMyFriendsReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    MyFriend myFriend = data.getValue(MyFriend.class);
                                                    if (myFriend.getUserId().equals(currentMyFriend.getUserId())) {
                                                        databaseMyFriendsReference.child(data.getKey()).removeValue();
                                                        break;
                                                    }
                                                }

                                                databaseMyFriendsReference.removeEventListener(this);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        databaseTraineeFriendsReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    MyFriend myFriend = data.getValue(MyFriend.class);
                                                    if (myFriend.getUserId().equals(mCurrentUserId)) {
                                                        databaseTraineeFriendsReference.child(data.getKey()).removeValue();
                                                        break;
                                                    }
                                                }

                                                databaseTraineeFriendsReference.removeEventListener(this);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                })
                                .create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(mMyFriendsFragment.getResources().getColor(R.color.purple));
                                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(mMyFriendsFragment.getResources().getColor(R.color.purple));
                            }
                        });

                        alertDialog.show();
                    }
                });
            }
        }
        else {
            holder.traineeCardView.setCardBackgroundColor(mMyFriendsFragment.getResources().getColor(R.color.white));

            holder.traineeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("traineeId", currentMyFriend.getUserId());
                    Intent intent = new Intent(mMyFriendsFragment.getContext(), MyFriendActivity.class);
                    intent.putExtras(bundle);
                    mMyFriendsFragment.getContext().startActivity(intent);
                }
            });
        }

        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String trainerName = dataSnapshot.child(currentMyFriend.getUserId() + "/Profile/name").getValue(String.class);
                String trainerImageUrl = dataSnapshot.child(currentMyFriend.getUserId() + "/Profile/profilePhotoUrl").getValue(String.class);

                holder.traineeNameTextView.setText(trainerName);
                Picasso.with(mMyFriendsFragment.getContext()).load(trainerImageUrl).fit().into(holder.traineeCircleImageView);

                mDatabaseTraineesReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMyFriendsList.size();
    }

    public void cleanUp() {
        mDatabaseTraineeFriendsReference.removeEventListener(mChildEventListener);
    }
}
