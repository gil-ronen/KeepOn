package com.gil_shiran_or.keepon.trainee.search_trainer;

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
import com.gil_shiran_or.keepon.trainee.my_trainers.MyTrainer;
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

public class TrainerProfileFragment extends Fragment {

    private DatabaseReference mDatabaseTrainerReference;
    private DatabaseReference mDatabaseTraineeReference;
    private ValueEventListener mTrainerValueEventListener;
    private ValueEventListener mAddTrainerFabValueEventListener;
    private String mTrainerId;
    private String mCurrentUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_trainer_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTrainerId = getArguments().getString("trainerId");
        mDatabaseTrainerReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId + "/Profile");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mDatabaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyTrainers");

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

        final FloatingActionButton addTrainerFloatingActionButton = getView().findViewById(R.id.trainer_add_button);

        mAddTrainerFabValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isTrainerExist = false;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("userId").getValue(String.class).equals(mTrainerId)) {
                        addTrainerFloatingActionButton.setVisibility(View.GONE);
                        isTrainerExist = true;
                        break;
                    }
                }

                if (!isTrainerExist) {
                    addTrainerFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                    .setTitle("Add Trainer")
                                    .setMessage("Are you sure you want to add this trainer?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            setMyTrainerToFirebase(new MyTrainer(mTrainerId));
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
                    mDatabaseTraineeReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseTraineeReference.addValueEventListener(mAddTrainerFabValueEventListener);
    }

    private void setMyTrainerToFirebase(MyTrainer myTrainer) {
        String key = mDatabaseTraineeReference.push().getKey();
        Map<String, Object> postValues = myTrainer.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(key, postValues);

        mDatabaseTraineeReference.updateChildren(childUpdates);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTrainerReference.removeEventListener(mTrainerValueEventListener);
        mDatabaseTraineeReference.removeEventListener(mAddTrainerFabValueEventListener);
    }
}
