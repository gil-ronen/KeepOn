package com.gil_shiran_or.keepon.trainee.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private DatabaseReference mDatabaseTraineeReference;
    private ValueEventListener mValueEventListener;
    private String mCurrentUserId;
    private String mTraineeGender;
    private String mTraineeProfilePhotoUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainee_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        final CircleImageView traineeCircleImageView = getView().findViewById(R.id.trainee_profile_img);
        final TextView traineeNameTextView = getView().findViewById(R.id.trainee_name);
        final TextView traineeEmailTextView = getView().findViewById(R.id.trainee_email);
        final TextView traineeCityTextView = getView().findViewById(R.id.trainee_city);
        final TextView traineeStreetTextView = getView().findViewById(R.id.trainee_street);
        final TextView traineeBirthDateTextView = getView().findViewById(R.id.trainee_birth_date);
        final TextView traineePhoneNumberTextView = getView().findViewById(R.id.trainee_phone_number);
        final ImageView traineeGenderImageView = getView().findViewById(R.id.trainee_gender);

        mDatabaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/Profile");
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTraineeProfilePhotoUrl = dataSnapshot.child("profilePhotoUrl").getValue(String.class);
                Picasso.with(getContext()).load(mTraineeProfilePhotoUrl).fit().into(traineeCircleImageView);
                traineeNameTextView.setText(dataSnapshot.child("name").getValue(String.class));
                traineeEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
                traineeCityTextView.setText(dataSnapshot.child("city").getValue(String.class));
                traineeStreetTextView.setText(dataSnapshot.child("street").getValue(String.class));
                traineeBirthDateTextView.setText(dataSnapshot.child("birthDate").getValue(String.class));
                traineePhoneNumberTextView.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                mTraineeGender = dataSnapshot.child("gender").getValue(String.class);

                if (mTraineeGender.equals("male")) {
                    traineeGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_male_sign));
                }
                else {
                    traineeGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_female_sign));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeReference.addValueEventListener(mValueEventListener);

        FloatingActionButton editProfileButton = getView().findViewById(R.id.profile_edit);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString("name", traineeNameTextView.getText().toString());
                bundle.putString("city", traineeCityTextView.getText().toString());
                bundle.putString("street", traineeStreetTextView.getText().toString());
                bundle.putString("phone", traineePhoneNumberTextView.getText().toString());
                bundle.putString("birthDate", traineeBirthDateTextView.getText().toString());
                bundle.putString("gender", mTraineeGender);
                bundle.putString("profilePhotoUrl", mTraineeProfilePhotoUrl);

                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTraineeReference.removeEventListener(mValueEventListener);
    }
}
