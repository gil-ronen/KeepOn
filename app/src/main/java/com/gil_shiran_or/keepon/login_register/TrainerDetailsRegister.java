package com.gil_shiran_or.keepon.login_register;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.gil_shiran_or.keepon.HomeActivity;
import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.Rating;
import com.gil_shiran_or.keepon.Trainer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TrainerDetailsRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static int PReqCode = 1;
    private static int REQUESCODE = 1;
    private Uri mPickedImgUri;

    private ImageView mImageUserPhoto;
    private EditText mNameView;
    private EditText mCompanyNameView;
    private EditText mPriceView;
    private EditText mTrainingCityView;
    private EditText mTrainingStreetView;
    private EditText mPhoneNumberView;
    private Spinner mSpinner;
    private String mPhoneCode;
    private ProgressBar loadingProgress;
    private Button updateButton;
    private Button mBirthDate;
    private String mGender;
    private RadioGroup mRadioUserGenderGroup;
    private RadioButton mRadioUserGenderButton;
    private RadioButton mRadioMale;
    private RadioButton mRadioFemale;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mAboutMeView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Trainer mTrainer;

    private String mEmail;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_details_register);

        mImageUserPhoto = findViewById(R.id.settings_user_photo);
        mNameView = findViewById(R.id.settings_trainerName);
        mCompanyNameView = findViewById(R.id.settings_company_name);
        mPriceView = findViewById(R.id.settings_price);
        mTrainingCityView = findViewById(R.id.settings_trainingCity);
        mTrainingStreetView = findViewById(R.id.settings_trainingStreet);
        mPhoneNumberView = findViewById(R.id.phoneText);
        mSpinner = findViewById(R.id.spinnerPhoneCode);
        mPhoneCode = "050";
        mBirthDate = findViewById(R.id.settings_birthdate);
        mGender = "";
        mRadioUserGenderGroup = findViewById(R.id.settings_user_gender_radioGroup);
        mRadioMale = findViewById(R.id.maleRadioButton);
        mRadioFemale = findViewById(R.id.femaleRadioButton);
        mAboutMeView = findViewById(R.id.settings_aboutme);
        loadingProgress = findViewById(R.id.settings_ProgressBar);
        updateButton = findViewById(R.id.settings_update_button);

        loadingProgress.setVisibility(View.INVISIBLE);


        Bundle bundle = getIntent().getExtras();
        mEmail = bundle.getString("email");
        mPassword = bundle.getString("password");


        mAuth = FirebaseAuth.getInstance(); // Get hold of an instance of FirebaseAuth
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainers");

        mImageUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        TrainerDetailsRegister.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("KeepOn: ", "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                mBirthDate.setText(date);
            }
        };

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.phoneCode, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPhoneCode = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void maleOrFemaleButton(View v) {
        int radioId = mRadioUserGenderGroup.getCheckedRadioButtonId();
        mRadioUserGenderButton = findViewById(radioId);
        if (mRadioUserGenderButton.getText().toString().equals(mRadioFemale.getText().toString())) {
            Log.d("KeepOn", "User gender is female");
            mGender = "female";
        } else if (mRadioUserGenderButton.getText().toString().equals(mRadioMale.getText().toString())) {
            Log.d("KeepOn", "User gender is male");
            mGender = "male";
        }
    }

    // Executed when Sign Up button is pressed.
    public void updateDetails(View v) {
        attemptUpdate();
    }


    private void attemptUpdate() {

        updateButton.setVisibility(View.INVISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);

        // Reset errors displayed in the form.
        mNameView.setError(null);
        mCompanyNameView.setError(null);
        mPriceView.setError(null);
        mTrainingCityView.setError(null);
        mTrainingStreetView.setError(null);
        mPhoneNumberView.setError(null);
        mBirthDate.setError(null);
        mAboutMeView.setError(null);

        // Store values at the time of the login attempt.
        String trainerName = mNameView.getText().toString();
        String companyName = mCompanyNameView.getText().toString();
        String price = mPriceView.getText().toString();
        String trainingCity = mTrainingCityView.getText().toString();
        String trainingStreet = mTrainingStreetView.getText().toString();
        String phoneNumber = mPhoneNumberView.getText().toString();
        String birthDate = mBirthDate.getText().toString();
        String gender = mGender;
        String aboutMe = mAboutMeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(trainerName)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(companyName)) {
            mCompanyNameView.setError(getString(R.string.error_field_required));
            focusView = mCompanyNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(price)) {
            mPriceView.setError(getString(R.string.error_field_required));
            focusView = mPriceView;
            cancel = true;
        }

        if (TextUtils.isEmpty(trainingCity)) {
            mTrainingCityView.setError(getString(R.string.error_field_required));
            focusView = mTrainingCityView;
            cancel = true;
        }

        if (TextUtils.isEmpty(trainingStreet)) {
            mTrainingStreetView.setError(getString(R.string.error_field_required));
            focusView = mTrainingStreetView;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 7) {
            mPhoneNumberView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneNumberView;
            cancel = true;
        }

        if (TextUtils.isEmpty(birthDate)) {
            mBirthDate.setError(getString(R.string.error_field_required));
            focusView = mBirthDate;
            cancel = true;
        }

        if (gender.isEmpty() || gender.equals("")) {
            showErrorDialog("Please verify your gender");
            cancel = true;
        }

        if (TextUtils.isEmpty(aboutMe)) {
            mAboutMeView.setError(getString(R.string.error_field_required));
            focusView = mAboutMeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            updateButton.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.INVISIBLE);

            if (focusView != null) {
                focusView.requestFocus();
            }
            Toast.makeText(this, "Please Verify All Field", Toast.LENGTH_SHORT).show();

        } else {
            try {
                uploadUserDetailsToFirebase();
                updateButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);

            } catch (Exception e) {
                updateButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
                showErrorDialog(e.getMessage());
            }
        }
    }

    private void uploadUserDetailsToFirebase() {
        final String trainerName = mNameView.getText().toString();
        final String companyName = mCompanyNameView.getText().toString();
        final int price = Integer.parseInt(mPriceView.getText().toString());
        final String trainingCity = mTrainingCityView.getText().toString();
        final String trainingStreet = mTrainingStreetView.getText().toString();
        final String phoneNumber = mPhoneNumberView.getText().toString();
        final String birthDate = mBirthDate.getText().toString();
        final String gender = mGender;
        final String aboutMe = mAboutMeView.getText().toString();

        if (mPickedImgUri != null) {
            mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("KeepOn", "createUser onComplete: " + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.d("KeepOn", "user creation failed");
                        showErrorDialog("Registration attempt failed");
                    } else {
                        Log.d("KeepOn", "user creation success");
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        Rating rating = new Rating(0,0,0,0,0,0,0,0);
                        mTrainer = new Trainer(trainerName, mEmail, mPassword, mPhoneCode + phoneNumber, birthDate, gender, "", companyName, aboutMe, trainingCity, trainingStreet, price, rating);

                        Map<String, Object> trainerValues = mTrainer.toMap();
                        Map<String, Object> ratingValues = rating.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put("Profile", trainerValues);
                        childUpdates.put("Rating", ratingValues);
                        current_user_db.updateChildren(childUpdates);


                        uploadUserPhoto(mPickedImgUri, mAuth.getCurrentUser());

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        } else {
            showErrorDialog("must upload a profile photo of you");
        }
    }


    // update user photo and name
    private void uploadUserPhoto(Uri pickedImgUri, final FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos").child(currentUser.getUid());
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);
                        Log.d("KeepOn: ", "download photo uri: " + uri.toString());
                        current_user_db.child("Profile").child("profilePhotoUrl").setValue(uri.toString());

                        mTrainer.setProfilePhotoUrl(uri.toString());

                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mNameView.getText().toString())
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            // user info failed update
                                            showErrorDialog("User info failed to update");
                                        }
                                    }
                                });

                        Intent traineeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(traineeIntent);
                        finish();
                    }
                });
            }
        });
    }


    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }


    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(TrainerDetailsRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(TrainerDetailsRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(TrainerDetailsRegister.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(TrainerDetailsRegister.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();

    }


    // Create an alert dialog to show in case registration failed
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null && data.getData() != null) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            mPickedImgUri = data.getData();
            mImageUserPhoto.setImageURI(mPickedImgUri);
        }
    }


}