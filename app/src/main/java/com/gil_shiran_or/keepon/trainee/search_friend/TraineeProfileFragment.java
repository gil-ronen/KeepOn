package com.gil_shiran_or.keepon.trainee.search_friend;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.my_friends.MyFriend;
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

public class TraineeProfileFragment extends Fragment {

    private DatabaseReference mDatabaseTraineeReference;
    private DatabaseReference mDatabaseMyFriendsReference;
    private DatabaseReference mDatabaseTraineeFriendsReference;
    private ValueEventListener mTraineeValueEventListener;
    private ValueEventListener mAddFriendFabValueEventListener;
    private String mTraineeId;
    private String mCurrentUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_friend_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTraineeId = getArguments().getString("traineeId");
        mDatabaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/Profile");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseMyFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyFriends");
        mDatabaseTraineeFriendsReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mTraineeId + "/MyFriends");

        final CircleImageView traineeCircleImageView = getView().findViewById(R.id.trainee_profile_img);
        final TextView traineeNameTextView = getView().findViewById(R.id.trainee_name);
        final TextView traineeEmailTextView = getView().findViewById(R.id.trainee_email);
        final TextView traineeCityTextView = getView().findViewById(R.id.trainee_city);
        final TextView traineeStreetTextView = getView().findViewById(R.id.trainee_street);
        final TextView traineeBirthDateTextView = getView().findViewById(R.id.trainee_birth_date);
        final TextView traineePhoneNumberTextView = getView().findViewById(R.id.trainee_phone_number);
        final ImageView traineeGenderImageView = getView().findViewById(R.id.trainee_gender);

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

        final FloatingActionButton addFriendFloatingActionButton = getView().findViewById(R.id.friend_add_button);

        mAddFriendFabValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isTrainerExist = false;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("userId").getValue(String.class).equals(mTraineeId)) {
                        addFriendFloatingActionButton.setVisibility(View.GONE);
                        isTrainerExist = true;
                        break;
                    }
                }

                if (!isTrainerExist) {
                    addFriendFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                    .setTitle("Add Friend")
                                    .setMessage("Are you sure you want to make this trainee your friend?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setMyFriendToFirebase(new MyFriend(mTraineeId, true));
                                            setTraineeFriendsToFirebase(new MyFriend(mCurrentUserId, false));
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
                else {
                    mDatabaseMyFriendsReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseMyFriendsReference.addValueEventListener(mAddFriendFabValueEventListener);
    }

    private void setMyFriendToFirebase(MyFriend myFriend) {
        String key = mDatabaseMyFriendsReference.push().getKey();
        Map<String, Object> myFriendValues = myFriend.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(key, myFriendValues);

        mDatabaseMyFriendsReference.updateChildren(childUpdates);
    }

    private void setTraineeFriendsToFirebase(MyFriend myFriend) {
        String key = mDatabaseTraineeFriendsReference.push().getKey();
        Map<String, Object> myFriendValues = myFriend.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(key, myFriendValues);

        mDatabaseTraineeFriendsReference.updateChildren(childUpdates);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTraineeReference.removeEventListener(mTraineeValueEventListener);
        mDatabaseMyFriendsReference.removeEventListener(mAddFriendFabValueEventListener);
    }
}
