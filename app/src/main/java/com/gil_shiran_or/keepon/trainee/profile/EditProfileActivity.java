package com.gil_shiran_or.keepon.trainee.profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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


import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static int PReqCode = 1;
    private static int REQUESCODE = 1;
    private Uri mPickedImgUri;


    private CircleImageView traineeCircleImageView;
    private EditText mNameView;
    private EditText mCityView;
    private EditText mStreetView;
    private EditText mPhoneNumberView;
    private Spinner mSpinner;
    private ProgressBar loadingProgress;
    private Button registerButton;
    private Button mBirthDate;
    private RadioGroup mRadioUserGenderGroup;
    private RadioButton mRadioUserGenderButton;
    private RadioButton mRadioMale;
    private RadioButton mRadioFemale;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String traineeProfilePhotoUrl;
    private String traineeName;
    private String traineeCity;
    private String traineeStreet;
    private String mPhoneCode;
    private String traineePhoneNumber;
    private String traineeBirthDate;
    private String traineeGender;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String mCurrentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trainee_profile);



        getWindow().setBackgroundDrawableResource(R.drawable.background_profile_blur);
        Toolbar toolbar = findViewById(R.id.edit_profile_toolbar);
        toolbar.setTitle("Edit My Profile");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);




        traineeCircleImageView = findViewById(R.id.traineeEditProfile_user_photo);
        mNameView = findViewById(R.id.traineeEditProfile_name);
        mCityView = findViewById(R.id.traineeEditProfile_city);
        mStreetView = findViewById(R.id.traineeEditProfile_street);
        mPhoneNumberView = findViewById(R.id.traineeEditProfile_phoneText);
        mSpinner = findViewById(R.id.traineeEditProfile_spinnerPhoneCode);
        mBirthDate = findViewById(R.id.traineeEditProfile_birthDate);
        mRadioUserGenderGroup = findViewById(R.id.traineeEditProfile_user_gender_radioGroup);
        mRadioMale = findViewById(R.id.traineeEditProfile_maleRadioButton);
        mRadioFemale = findViewById(R.id.traineeEditProfile_femaleRadioButton);
        loadingProgress = findViewById(R.id.traineeEditProfile_ProgressBar);
        registerButton = findViewById(R.id.traineeEditProfile_update_button);

        Bundle bundle = getIntent().getExtras();

        traineeProfilePhotoUrl = bundle.getString("profilePhotoUrl");

        Picasso.with(this).load(traineeProfilePhotoUrl).fit().into(traineeCircleImageView);

        String allPhoneNumber = bundle.getString("phone");//"004-034556";
        String[] parts = allPhoneNumber.split("-");
        String code = parts[0]; // 004
        String phone = parts[1]; // 034556


        mNameView.setText(bundle.getString("name"));
        mCityView.setText(bundle.getString("city"));
        mStreetView.setText(bundle.getString("street"));
        mPhoneCode = code;
        mPhoneNumberView.setText(phone);
        mBirthDate.setText(bundle.getString("birthDate"));
        traineeGender = bundle.getString("gender");

        if(traineeGender.equals("female"))
        {
            mRadioUserGenderGroup.check(R.id.traineeEditProfile_femaleRadioButton);
        }
        if(traineeGender.equals("male"))
        {
            mRadioUserGenderGroup.check(R.id.traineeEditProfile_maleRadioButton);
        }


        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance(); // Get hold of an instance of FirebaseAuth
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users/Trainees/" + mCurrentUserId + "/Profile");


        traineeCircleImageView.setOnClickListener(new View.OnClickListener() {
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
                        EditProfileActivity.this,
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
            traineeGender = "female";
        } else if (mRadioUserGenderButton.getText().toString().equals(mRadioMale.getText().toString())) {
            Log.d("KeepOn", "User gender is male");
            traineeGender = "male";
        }
    }

    public void onUpdateUserDetailsClicked(View v) {
        registerButton.setVisibility(View.INVISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);
        checkValidInput();
    }



    private void checkValidInput() {


        // Reset errors displayed in the form.
        mNameView.setError(null);
        mCityView.setError(null);
        mStreetView.setError(null);
        mPhoneNumberView.setError(null);
        mBirthDate.setError(null);

        // Store values at the time of the login attempt.
        traineeName = mNameView.getText().toString();
        traineeCity = mCityView.getText().toString();
        traineeStreet = mStreetView.getText().toString();
        traineePhoneNumber = mPhoneNumberView.getText().toString();
        traineeBirthDate = mBirthDate.getText().toString();
        String trainee_gender = traineeGender;

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(traineeName)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(traineeCity)) {
            mCityView.setError(getString(R.string.error_field_required));
            focusView = mCityView;
            cancel = true;
        }

        if (TextUtils.isEmpty(traineeStreet)) {
            mStreetView.setError(getString(R.string.error_field_required));
            focusView = mStreetView;
            cancel = true;
        }

        if (TextUtils.isEmpty(traineePhoneNumber) || traineePhoneNumber.length() != 7) {
            mPhoneNumberView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneNumberView;
            cancel = true;
        }

        if (TextUtils.isEmpty(traineeBirthDate)) {
            mBirthDate.setError(getString(R.string.error_field_required));
            focusView = mBirthDate;
            cancel = true;
        }

        if (trainee_gender.isEmpty() || trainee_gender.equals("")) {
            showErrorDialog("Please verify your gender");
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.


            if (focusView != null) {
                focusView.requestFocus();
            }
            Toast.makeText(this, "Please Verify All Field", Toast.LENGTH_SHORT).show();
            registerButton.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.INVISIBLE);
        } else {
            try {
                updateUserDetailsToFirebase();

            } catch (Exception e) {
                registerButton.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
                showErrorDialog(e.getMessage());
            }
        }
    }

    private void updateUserDetailsToFirebase() {

        mDatabase.child("birthDate").setValue(traineeBirthDate);
        mDatabase.child("city").setValue(traineeCity);
        mDatabase.child("gender").setValue(traineeGender);
        mDatabase.child("name").setValue(traineeName);
        mDatabase.child("phoneNumber").setValue(mPhoneCode + "-" + traineePhoneNumber);
        mDatabase.child("street").setValue(traineeStreet);

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
                                            registerButton.setVisibility(View.VISIBLE);
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

        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(EditProfileActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this,
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
            traineeCircleImageView.setImageURI(mPickedImgUri);
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
