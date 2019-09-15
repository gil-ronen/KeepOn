package com.gil_shiran_or.keepon.trainer.profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
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

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.db.Rating;
import com.gil_shiran_or.keepon.db.Trainer;
import com.gil_shiran_or.keepon.trainer.nav.TrainerNavActivity;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditTrainerProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static int PReqCode = 1;
    private static int REQUESCODE = 1;
    private Uri mPickedImgUri;

    private CircleImageView trainerCircleImageView;
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
    private String trainerGender;
    private RadioGroup mRadioUserGenderGroup;
    private RadioButton mRadioUserGenderButton;
    private RadioButton mRadioMale;
    private RadioButton mRadioFemale;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText mAboutMeView;

    private String trainerName;
    private String companyName;
    private int price;
    private String trainingCity;
    private String trainingStreet;
    private String phoneNumber;
    private String birthDate;
    private String gender;
    private String aboutMe;

    private String trainerProfilePhotoUrl;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String mCurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trainer_profile);

        getWindow().setBackgroundDrawableResource(R.drawable.background_profile_blur);
        Toolbar toolbar = findViewById(R.id.trainerEditProfile_toolbar);
        toolbar.setTitle("Edit My Profile");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        trainerCircleImageView = findViewById(R.id.trainerEditProfile_user_photo);
        mNameView = findViewById(R.id.trainerEditProfile_trainerName);
        mCompanyNameView = findViewById(R.id.trainerEditProfile_company_name);
        mPriceView = findViewById(R.id.trainerEditProfile_price);
        mTrainingCityView = findViewById(R.id.trainerEditProfile_trainingCity);
        mTrainingStreetView = findViewById(R.id.trainerEditProfile_trainingStreet);
        mPhoneNumberView = findViewById(R.id.trainerEditProfile_phoneText);
        mSpinner = findViewById(R.id.trainerEditProfile_spinnerPhoneCode);
        mBirthDate = findViewById(R.id.trainerEditProfile_birthdate);
        mRadioUserGenderGroup = findViewById(R.id.trainerEditProfile_user_gender_radioGroup);
        mRadioMale = findViewById(R.id.trainerEditProfile_maleRadioButton);
        mRadioFemale = findViewById(R.id.trainerEditProfile_femaleRadioButton);
        mAboutMeView = findViewById(R.id.trainerEditProfile_aboutme);
        loadingProgress = findViewById(R.id.trainerEditProfile_ProgressBar);
        updateButton = findViewById(R.id.trainerEditProfile_update_button);


        Bundle bundle = getIntent().getExtras();

        trainerProfilePhotoUrl = bundle.getString("profilePhotoUrl");

        Picasso.with(this).load(trainerProfilePhotoUrl).fit().into(trainerCircleImageView);

        String allPhoneNumber = bundle.getString("phone");//"004-034556";
        String[] parts = allPhoneNumber.split("-");
        String code = parts[0]; // 004
        String phone = parts[1]; // 034556

        mNameView.setText(bundle.getString("name"));
        mTrainingCityView.setText(bundle.getString("city"));
        mTrainingStreetView.setText(bundle.getString("street"));
        mPhoneCode = code;
        mPhoneNumberView.setText(phone);
        mBirthDate.setText(bundle.getString("birthDate"));
        trainerGender = bundle.getString("gender");
        mAboutMeView.setText(bundle.getString("aboutMe"));
        mCompanyNameView.setText(bundle.getString("companyName"));
        mPriceView.setText("" + bundle.getInt("price"));

        if(trainerGender.equals("female"))
        {
            mRadioUserGenderGroup.check(R.id.trainerEditProfile_femaleRadioButton);
        }
        if(trainerGender.equals("male"))
        {
            mRadioUserGenderGroup.check(R.id.trainerEditProfile_maleRadioButton);
        }



        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance(); // Get hold of an instance of FirebaseAuth
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users/Trainers/" + mCurrentUserId + "/Profile");





        trainerCircleImageView.setOnClickListener(new View.OnClickListener() {
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
                        EditTrainerProfileActivity.this,
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

        setSpinText(mSpinner, code);
    }

    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }
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
            trainerGender = "female";
        } else if (mRadioUserGenderButton.getText().toString().equals(mRadioMale.getText().toString())) {
            Log.d("KeepOn", "User gender is male");
            trainerGender = "male";
        }
    }

    // Executed when Sign Up button is pressed.
    public void onUpdateUserDetailsClicked(View v) {
        updateButton.setVisibility(View.INVISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);
        checkValidInput();
    }


    private void checkValidInput() {

        // Reset errors displayed in the form.
        mNameView.setError(null);
        mCompanyNameView.setError(null);
        mPriceView.setError(null);
        mTrainingCityView.setError(null);
        mTrainingStreetView.setError(null);
        mPhoneNumberView.setError(null);
        mBirthDate.setError(null);
        mAboutMeView.setError(null);


        trainerName = mNameView.getText().toString();
        companyName = mCompanyNameView.getText().toString();
        price = Integer.parseInt(mPriceView.getText().toString());
        trainingCity = mTrainingCityView.getText().toString();
        trainingStreet = mTrainingStreetView.getText().toString();
        phoneNumber = mPhoneNumberView.getText().toString();
        birthDate = mBirthDate.getText().toString();
        gender = trainerGender;
        aboutMe = mAboutMeView.getText().toString();

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

        if (TextUtils.isEmpty(mPriceView.getText().toString())) {
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

            if (focusView != null) {
                focusView.requestFocus();
            }
            Toast.makeText(this, "Please Verify All Field", Toast.LENGTH_SHORT).show();
            updateButton.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.INVISIBLE);

        } else {
            try {
                updateUserDetailsToFirebase();

            } catch (Exception e) {
                updateButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
                showErrorDialog(e.getMessage());
            }
        }
    }

    private void updateUserDetailsToFirebase() {

        mDatabase.child("birthDate").setValue(birthDate);
        mDatabase.child("trainingCity").setValue(trainingCity);
        mDatabase.child("gender").setValue(gender);
        mDatabase.child("name").setValue(trainerName);
        mDatabase.child("phoneNumber").setValue(mPhoneCode + "-" + phoneNumber);
        mDatabase.child("trainingStreet").setValue(trainingStreet);
        mDatabase.child("companyName").setValue(companyName);
        mDatabase.child("price").setValue(price);
        mDatabase.child("aboutMe").setValue(aboutMe);

        if (mPickedImgUri != null) {
            uploadUserPhoto(mPickedImgUri, mAuth.getCurrentUser());
        }
        finish();
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
                        DatabaseReference current_user_db = mDatabase;
                        Log.d("KeepOn: ", "download photo uri: " + uri.toString());
                        current_user_db.child("profilePhotoUrl").setValue(uri.toString());


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
                                            updateButton.setVisibility(View.VISIBLE);
                                            loadingProgress.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
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

        if (ContextCompat.checkSelfPermission(EditTrainerProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditTrainerProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(EditTrainerProfileActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(EditTrainerProfileActivity.this,
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
            trainerCircleImageView.setImageURI(mPickedImgUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

