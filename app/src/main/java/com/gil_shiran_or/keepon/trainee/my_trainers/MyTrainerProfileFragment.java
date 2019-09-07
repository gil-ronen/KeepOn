package com.gil_shiran_or.keepon.trainee.my_trainers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.gil_shiran_or.keepon.trainer_weekly_planner.trainee_side.MainWeeklySlotsPickerActivity;
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
    private boolean mIsFabOpen = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_trainer_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTrainerId = getArguments().getString("trainerId");
        mDatabaseTrainerReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mTrainerId + "/Profile");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        final CircleImageView trainerCircleImageView = getView().findViewById(R.id.my_trainer_profile_img);
        final TextView trainerNameTextView = getView().findViewById(R.id.my_trainer_name);
        final TextView trainerAboutMeTextView = getView().findViewById(R.id.my_trainer_about_me);
        final TextView trainerEmailTextView = getView().findViewById(R.id.my_trainer_email);
        final TextView trainerGymNameTextView = getView().findViewById(R.id.my_trainer_company_name);
        final TextView trainerTrainingCityTextView = getView().findViewById(R.id.my_trainer_training_city);
        final TextView trainerTrainingStreetTextView = getView().findViewById(R.id.my_trainer_training_street);
        final TextView trainerBirthDateTextView = getView().findViewById(R.id.my_trainer_birth_date);
        final TextView trainerPhoneNumberTextView = getView().findViewById(R.id.my_trainer_phone_number);
        final TextView trainerPriceTextView = getView().findViewById(R.id.my_trainer_price);
        final ImageView trainerGenderImageView = getView().findViewById(R.id.my_trainer_gender);

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(getContext()).load(dataSnapshot.child("profilePhotoUrl").getValue(String.class)).fit().into(trainerCircleImageView);
                trainerNameTextView.setText(dataSnapshot.child("name").getValue(String.class));
                trainerAboutMeTextView.setText(dataSnapshot.child("aboutMe").getValue(String.class));
                trainerEmailTextView.setText(dataSnapshot.child("email").getValue(String.class));
                trainerGymNameTextView.setText(dataSnapshot.child("companyName").getValue(String.class));
                trainerTrainingCityTextView.setText(dataSnapshot.child("trainingCity").getValue(String.class));
                trainerTrainingStreetTextView.setText(dataSnapshot.child("trainingStreet").getValue(String.class));
                trainerBirthDateTextView.setText(dataSnapshot.child("birthDate").getValue(String.class));
                trainerPhoneNumberTextView.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                trainerPriceTextView.setText(dataSnapshot.child("price").getValue(Integer.class) + "\u20aa");

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

        final FloatingActionButton optionsFloatingActionButton = getView().findViewById(R.id.my_trainer_show_options);
        final FloatingActionButton quitTrainerFloatingActionButton = getView().findViewById(R.id.my_trainer_quit_button);
        final FloatingActionButton scheduleFloatingActionButton = getView().findViewById(R.id.my_trainer_schedule_button);
        final Animation fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        final Animation fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        final Animation fabClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clockwise);
        final Animation fabAntiClockwise = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anti_clockwise);

        optionsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsFabOpen) {
                    quitTrainerFloatingActionButton.startAnimation(fabClose);
                    scheduleFloatingActionButton.startAnimation(fabClose);
                    optionsFloatingActionButton.startAnimation(fabAntiClockwise);

                    mIsFabOpen = false;
                }
                else {
                    quitTrainerFloatingActionButton.startAnimation(fabOpen);
                    scheduleFloatingActionButton.startAnimation(fabOpen);
                    optionsFloatingActionButton.startAnimation(fabClockwise);

                    mIsFabOpen = true;
                }
            }
        });


        quitTrainerFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Quit Trainer")
                        .setMessage("Are you sure you want to quit this trainer?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final DatabaseReference databaseTraineeReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/MyTrainers");

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

        final Fragment fragment = this;
        scheduleFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("trainerId", mTrainerId);
                Intent intent = new Intent(fragment.getContext(), MainWeeklySlotsPickerActivity.class);
                intent.putExtras(bundle);
                fragment.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseTrainerReference.removeEventListener(mValueEventListener);
    }
}
