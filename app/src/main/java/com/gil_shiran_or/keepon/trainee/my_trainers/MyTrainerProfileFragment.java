package com.gil_shiran_or.keepon.trainee.my_trainers;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTrainerProfileFragment extends Fragment {

    private DatabaseReference mDatabaseTrainerReference;
    private ValueEventListener mValueEventListener;
    private String mTrainerId;
    private String mCurrentUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainer_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTrainerId = getArguments().getString("trainerId");
        mDatabaseTrainerReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        final CircleImageView trainerCircleImageView = getView().findViewById(R.id.my_trainer_profile_img);
        final TextView trainerNameTextView = getView().findViewById(R.id.my_trainer_name);
        final TextView trainerAboutMeTextView = getView().findViewById(R.id.my_trainer_about_me);
        final TextView trainerEmailTextView = getView().findViewById(R.id.my_trainer_email);
        final TextView trainerGymNameTextView = getView().findViewById(R.id.my_trainer_gym_name);
        final TextView trainerGymAddressTextView = getView().findViewById(R.id.my_trainer_gym_address);
        final TextView trainerBirthDateTextView = getView().findViewById(R.id.my_trainer_birth_date);
        final TextView trainerPhoneNumberTextView = getView().findViewById(R.id.my_trainer_phone_number);
        final ImageView trainerGenderImageView = getView().findViewById(R.id.my_trainer_gender);

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(getContext()).load(dataSnapshot.child("profilePhotoUri").getValue(String.class)).fit().into(trainerCircleImageView);
                trainerNameTextView.setText(dataSnapshot.child("username").getValue(String.class));
                trainerAboutMeTextView.setText(dataSnapshot.child("aboutMe").getValue(String.class));
                trainerEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
                trainerGymNameTextView.setText(dataSnapshot.child("companyName").getValue(String.class));
                trainerGymAddressTextView.setText(dataSnapshot.child("trainingPlaceAddress").getValue(String.class));
                trainerBirthDateTextView.setText(dataSnapshot.child("birthDate").getValue(String.class));
                trainerPhoneNumberTextView.setText(dataSnapshot.child("phoneNumber").getValue(String.class));

                if (dataSnapshot.child("gender").getValue(String.class).equals("male")) {
                    trainerGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_male_sign));
                }
                else {
                    trainerGenderImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_female_sign));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTrainerReference.addValueEventListener(mValueEventListener);

        FloatingActionButton quitTrainerFloatingActionButton = getView().findViewById(R.id.my_trainer_quit_button);

        quitTrainerFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Quit Trainer")
                        .setMessage("Are you sure you want to quit this trainer?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final DatabaseReference databaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/myTrainers");

                                databaseTraineeReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            if (data.child("userId").getValue(String.class).equals(mTrainerId)) {
                                                data.getRef().removeValue();
                                                break;
                                            }
                                        }

                                        databaseTraineeReference.removeEventListener(this);

                                        getActivity().finish();
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
        mDatabaseTrainerReference.removeEventListener(mValueEventListener);
    }
}
