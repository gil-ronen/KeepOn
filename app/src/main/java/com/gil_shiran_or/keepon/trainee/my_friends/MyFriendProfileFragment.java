package com.gil_shiran_or.keepon.trainee.my_friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFriendProfileFragment extends Fragment {

    private DatabaseReference mDatabaseTraineeReference;
    private DatabaseReference mDatabaseMyFriendsReference;
    private DatabaseReference mDatabaseTraineeFriendsReference;
    private ValueEventListener mTraineeValueEventListener;
    private ValueEventListener mAddFriendFabValueEventListener;
    private String mTraineeId;
    private String mCurrentUserId;
    private boolean mIsFabOpen = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_friend_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTraineeId = getArguments().getString("traineeId");
        mDatabaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/Profile");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseMyFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyFriends");
        mDatabaseTraineeFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/MyFriends");

        final CircleImageView traineeCircleImageView = getView().findViewById(R.id.my_friend_profile_img);
        final TextView traineeNameTextView = getView().findViewById(R.id.my_friend_name);
        final TextView traineeEmailTextView = getView().findViewById(R.id.my_friend_email);
        final TextView traineeCityTextView = getView().findViewById(R.id.my_friend_city);
        final TextView traineeStreetTextView = getView().findViewById(R.id.my_friend_street);
        final TextView traineeBirthDateTextView = getView().findViewById(R.id.my_friend_birth_date);
        final TextView traineePhoneNumberTextView = getView().findViewById(R.id.my_friend_phone_number);
        final ImageView traineeGenderImageView = getView().findViewById(R.id.my_friend_gender);

        mTraineeValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(getContext()).load(dataSnapshot.child("profilePhotoUrl").getValue(String.class)).fit().into(traineeCircleImageView);
                traineeNameTextView.setText(dataSnapshot.child("name").getValue(String.class));
                traineeEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
                traineeCityTextView.setText(dataSnapshot.child("city").getValue(String.class));
                traineeStreetTextView.setText(dataSnapshot.child("street").getValue(String.class));
                traineeBirthDateTextView.setText(dataSnapshot.child("birthDate").getValue(String.class));
                traineePhoneNumberTextView.setText(dataSnapshot.child("phoneNumber").getValue(String.class));

                if (dataSnapshot.child("gender").getValue(String.class).equals("male")) {
                    traineeGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_male_sign));
                } else {
                    traineeGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_female_sign));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeReference.addValueEventListener(mTraineeValueEventListener);

        final FloatingActionButton optionsFloatingActionButton = getView().findViewById(R.id.my_friend_show_options);
        final FloatingActionButton removeFriendFloatingActionButton = getView().findViewById(R.id.my_friend_quit_button);
        final FloatingActionButton chatFloatingActionButton = getView().findViewById(R.id.my_friend_chat);
        final Animation fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        final Animation fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        final Animation fabClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        final Animation fabAntiClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anti_clockwise);

        optionsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsFabOpen) {
                    removeFriendFloatingActionButton.startAnimation(fabClose);
                    chatFloatingActionButton.startAnimation(fabClose);
                    optionsFloatingActionButton.startAnimation(fabAntiClockwise);

                    mIsFabOpen = false;
                }
                else {
                    removeFriendFloatingActionButton.startAnimation(fabOpen);
                    chatFloatingActionButton.startAnimation(fabOpen);
                    optionsFloatingActionButton.startAnimation(fabClockwise);

                    mIsFabOpen = true;
                }
            }
        });

        removeFriendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Remove Friend")
                        .setMessage("Are you sure you want to remove this friend?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final DatabaseReference databaseMyFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyFriends");
                                final DatabaseReference databaseTraineeFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/MyFriends");

                                databaseMyFriendsReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            MyFriend myFriend = data.getValue(MyFriend.class);
                                            if (myFriend.getUserId().equals(mTraineeId)) {
                                                databaseMyFriendsReference.child(data.getKey()).removeValue();
                                                break;
                                            }
                                        }

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

                                                getActivity().finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        databaseMyFriendsReference.removeEventListener(this);
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

                            }
                        })
                        .create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple));
                        alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purple));
                    }
                });

                alertDialog.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTraineeReference.removeEventListener(mTraineeValueEventListener);
        //mDatabaseMyFriendsReference.removeEventListener(mAddFriendFabValueEventListener);
    }
}
