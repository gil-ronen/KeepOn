package com.gil_shiran_or.keepon.trainer.profile;

import android.content.Intent;
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
import com.gil_shiran_or.keepon.trainee.profile.EditProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private DatabaseReference mDatabaseTrainerReference;
    private ValueEventListener mTrainerValueEventListener;
    private String mCurrentUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainer_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseTrainerReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mCurrentUserId + "/Profile");

        final CircleImageView trainerCircleImageView = getView().findViewById(R.id.trainer_profile_img);
        final TextView trainerNameTextView = getView().findViewById(R.id.trainer_name);
        final TextView trainerAboutMeTextView = getView().findViewById(R.id.trainer_about_me);
        final TextView trainerEmailTextView = getView().findViewById(R.id.trainer_email);
        final TextView trainerCompanyNameTextView = getView().findViewById(R.id.trainer_company_name);
        final TextView trainerTrainingCityTextView = getView().findViewById(R.id.trainer_training_city);
        final TextView trainerTrainingStreetTextView = getView().findViewById(R.id.trainer_training_street);
        final TextView trainerBirthDateTextView = getView().findViewById(R.id.trainer_birth_date);
        final TextView trainerPhoneNumberTextView = getView().findViewById(R.id.trainer_phone_number);
        final TextView trainerPriceTextView = getView().findViewById(R.id.trainer_price);
        final ImageView trainerGenderImageView = getView().findViewById(R.id.trainer_gender);

        mTrainerValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(getContext()).load(dataSnapshot.child("profilePhotoUrl").getValue(String.class)).fit().into(trainerCircleImageView);
                trainerNameTextView.setText(dataSnapshot.child("name").getValue(String.class));
                trainerAboutMeTextView.setText(dataSnapshot.child("aboutMe").getValue(String.class));
                trainerEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
                trainerCompanyNameTextView.setText(dataSnapshot.child("companyName").getValue(String.class));
                trainerTrainingCityTextView.setText(dataSnapshot.child("trainingCity").getValue(String.class));
                trainerTrainingStreetTextView.setText(dataSnapshot.child("trainingStreet").getValue(String.class));
                trainerBirthDateTextView.setText(dataSnapshot.child("birthDate").getValue(String.class));
                trainerPhoneNumberTextView.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                trainerPriceTextView.setText(dataSnapshot.child("price").getValue(Integer.class) + "\u20aa");

                if (dataSnapshot.child("gender").getValue(String.class).equals("male")) {
                    trainerGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_male_sign));
                } else {
                    trainerGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_female_sign));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTrainerReference.addValueEventListener(mTrainerValueEventListener);

        FloatingActionButton editProfileButton = getView().findViewById(R.id.trainer_profile_edit);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTrainerReference.removeEventListener(mTrainerValueEventListener);
    }
}
