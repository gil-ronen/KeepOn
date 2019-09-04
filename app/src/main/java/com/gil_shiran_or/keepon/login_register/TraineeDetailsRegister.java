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
        import com.gil_shiran_or.keepon.Trainee;
        import com.gil_shiran_or.keepon.trainee.nav.TraineeNavActivity;
        import com.gil_shiran_or.keepon.Status;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;

        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.auth.UserProfileChangeRequest;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Map;


public class TraineeDetailsRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static int PReqCode = 1;
    private static int REQUESCODE = 1;
    private Uri mPickedImgUri;

    private ImageView mImageUserPhoto;
    private EditText mNameView;
    private EditText mCityView;
    private EditText mStreetView;
    private EditText mPhoneNumberView;
    private Spinner mSpinner;
    private ProgressBar loadingProgress;
    private Button updateButton;
    private Button mBirthDate;
    private RadioGroup mRadioUserGenderGroup;
    private RadioButton mRadioUserGenderButton;
    private RadioButton mRadioMale;
    private RadioButton mRadioFemale;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private String traineeName;
    private String traineeCity;
    private String traineeStreet;
    private String mPhoneCode;
    private String traineePhoneNumber;
    private String traineeBirthDate;
    private String traineeGender;

    private String mEmail;
    private String mPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Trainee mTrainee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_details_register);

        mImageUserPhoto = findViewById(R.id.settings_user_photo);
        mNameView = findViewById(R.id.settings_name);
        mCityView = findViewById(R.id.settings_city);
        mStreetView = findViewById(R.id.settings_street);
        mPhoneNumberView = findViewById(R.id.phoneText);
        mSpinner = findViewById(R.id.spinnerPhoneCode);
        mBirthDate = findViewById(R.id.settings_birthDate);
        mRadioUserGenderGroup = findViewById(R.id.settings_user_gender_radioGroup);
        mRadioMale = findViewById(R.id.maleRadioButton);
        mRadioFemale = findViewById(R.id.femaleRadioButton);
        loadingProgress = findViewById(R.id.settings_ProgressBar);
        updateButton = findViewById(R.id.settings_update_button);

        Bundle bundle = getIntent().getExtras();
        mEmail = bundle.getString("email");
        mPassword = bundle.getString("password");

        traineeName = mNameView.getText().toString();
        traineeCity = mCityView.getText().toString();
        traineeStreet = mStreetView.getText().toString();
        mPhoneCode = "050";
        traineePhoneNumber = mPhoneNumberView.getText().toString();
        traineeBirthDate = mBirthDate.getText().toString();
        traineeGender = "";

        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance(); // Get hold of an instance of FirebaseAuth
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainees");


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
                        TraineeDetailsRegister.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
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
            traineeGender = "female";
        } else if (mRadioUserGenderButton.getText().toString().equals(mRadioMale.getText().toString())) {
            Log.d("KeepOn", "User gender is male");
            traineeGender = "male";
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

        if (mPickedImgUri != null) {
            mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("KeepOn", "createUser onComplete: " + task.isSuccessful());

                    if(!task.isSuccessful())
                    {
                        Log.d("KeepOn", "user creation failed");
                        showErrorDialog("Registration attempt failed");
                        //updateButton.setVisibility(View.VISIBLE);
                        //loadingProgress.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        Log.d("KeepOn", "user creation success");


                        generateTraineeSectionInDatabase();

                        //uploadUserPhoto(mPickedImgUri, mAuth.getCurrentUser());

                        //Intent traineeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                        //startActivity(traineeIntent);
                        //finish();
                    }
                }
            });

        }
        else {
            showErrorDialog("must upload a profile photo of you");
        }
    }

    public void generateTraineeSectionInDatabase()
    {
        final DatabaseReference databaseScoreToNextLevelReference = FirebaseDatabase.getInstance().getReference().child("Levels").child("Level1").child("scoreToNextLevel");
        databaseScoreToNextLevelReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String user_id = mAuth.getCurrentUser().getUid();
                DatabaseReference current_user_db = mDatabase.child(user_id);

                Status status = new Status("Level1",0,dataSnapshot.getValue(Integer.class));
                mTrainee = new Trainee(traineeName, mEmail, mPassword, mPhoneCode + traineePhoneNumber, traineeBirthDate, traineeGender, "", traineeCity, traineeStreet, status );

                Map<String, Object> traineeValues = mTrainee.toMap();
                Map<String, Object> ratingValues = status.toMap();
                Map<String, Object> childUpdates = new HashMap<>();

                childUpdates.put("Profile", traineeValues);
                childUpdates.put("Status", ratingValues);
                current_user_db.updateChildren(childUpdates);

                uploadUserPhoto(mPickedImgUri, mAuth.getCurrentUser());


                //value = dataSnapshot.getValue(String.class);
                databaseScoreToNextLevelReference.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
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

                        mTrainee.setProfilePhotoUrl(uri.toString());

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

                        Intent traineeIntent = new Intent(getApplicationContext(), TraineeNavActivity.class);
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
        startActivityForResult(galleryIntent,REQUESCODE);
    }


    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(TraineeDetailsRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(TraineeDetailsRegister.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(TraineeDetailsRegister.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(TraineeDetailsRegister.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();

    }


    // Create an alert dialog to show in case registration failed
    private void showErrorDialog(String message)
    {
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