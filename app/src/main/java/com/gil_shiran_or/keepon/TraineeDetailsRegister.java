package com.gil_shiran_or.keepon;

        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.text.TextUtils;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;


        import android.support.annotation.NonNull;
        import android.support.v7.app.AlertDialog;

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


public class TraineeDetailsRegister extends AppCompatActivity {

    private static int PReqCode = 1;
    private static int REQUESCODE = 1;
    private Uri mPickedImgUri;

    private ImageView mImageUserPhoto;
    private EditText mWeightView;
    private EditText mHeightView;
    private EditText mResidenceView;
    private EditText mPhoneNumberView;
    private ProgressBar loadingProgress;
    private Button updateButton;
    private EditText mAgeView;
    private String mGender;
    private RadioGroup mRadioUserGenderGroup;
    private RadioButton mRadioUserGenderButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String mEmail;
    private String mPassword;
    private String mUsername;
    private String mUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_details_register);

        mImageUserPhoto = findViewById(R.id.settings_user_photo);
        mWeightView = findViewById(R.id.settings_weight);
        mHeightView = findViewById(R.id.settings_height);
        mResidenceView = findViewById(R.id.settings_residence);
        mPhoneNumberView = findViewById(R.id.settings_mobile_phone);
        mAgeView = findViewById(R.id.settings_age);
        mGender = "";
        mRadioUserGenderGroup = findViewById(R.id.settings_user_gender_radioGroup);
        loadingProgress = findViewById(R.id.settings_ProgressBar);
        updateButton = findViewById(R.id.settings_update_button);

        loadingProgress.setVisibility(View.INVISIBLE);

        //TODO: Bundle from the previous intent
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        mEmail = bundle.getString("email");
        mPassword = bundle.getString("password");
        mUsername = bundle.getString("username");
        mUserType = bundle.getString("userType");

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
    }

    public void maleOrFemaleButton(View v) {
        int radioId = mRadioUserGenderGroup.getCheckedRadioButtonId();
        mRadioUserGenderButton = findViewById(radioId);
        if (mRadioUserGenderButton.getText().toString().equals("I'm female")) {
            Log.d("KeepOn", "User gender is female");
            mGender = "female";
        } else if (mRadioUserGenderButton.getText().toString().equals("I'm male")) {
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
        mWeightView.setError(null);
        mHeightView.setError(null);
        mResidenceView.setError(null);
        mPhoneNumberView.setError(null);
        mAgeView.setError(null);

        // Store values at the time of the login attempt.
        String weight = mWeightView.getText().toString();
        String height = mHeightView.getText().toString();
        String residence = mResidenceView.getText().toString();
        String phoneNumber = mPhoneNumberView.getText().toString();
        String age = mAgeView.getText().toString();
        String gender = mGender;

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(weight)) {
            mWeightView.setError(getString(R.string.error_field_required));
            focusView = mWeightView;
            cancel = true;
        }

        if (TextUtils.isEmpty(height)) {
            mHeightView.setError(getString(R.string.error_field_required));
            focusView = mHeightView;
            cancel = true;
        }

        if (TextUtils.isEmpty(residence)) {
            mResidenceView.setError(getString(R.string.error_field_required));
            focusView = mResidenceView;
            cancel = true;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberView.setError(getString(R.string.error_field_required));
            focusView = mPhoneNumberView;
            cancel = true;
        }

        if (TextUtils.isEmpty(age)) {
            mAgeView.setError(getString(R.string.error_field_required));
            focusView = mAgeView;
            cancel = true;
        }

        if (gender.isEmpty() || gender.equals("")) {
            showErrorDialog("Please Verify Your Gender");
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
        final String weight = mWeightView.getText().toString();
        final String height = mHeightView.getText().toString();
        final String residence = mResidenceView.getText().toString();
        final String phoneNumber = mPhoneNumberView.getText().toString();
        final String age = mAgeView.getText().toString();
        final String gender = mGender;

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
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        current_user_db.child("username").setValue(mUsername);
                        current_user_db.child("email").setValue(mEmail);
                        current_user_db.child("user_type").setValue(mUserType);
                        current_user_db.child("weight").setValue(weight);
                        current_user_db.child("height").setValue(height);
                        current_user_db.child("residence").setValue(residence);
                        current_user_db.child("phoneNumber").setValue(phoneNumber);
                        current_user_db.child("age").setValue(age);
                        current_user_db.child("gender").setValue(gender);
                        current_user_db.child("profile_photo").setValue(mPickedImgUri.toString());

                        uploadUserPhoto(mPickedImgUri, mAuth.getCurrentUser());

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
        else {
            showErrorDialog("Must Upload A Profile Photo Of You");
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

                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mUsername)
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
                    }
                });
            }
        });
        //Log.d("KeepOn: ", "download photo uri: " + imageFilePath.getDownloadUrl().toString());
        //mDatabase.child("profile_photo").setValue(imageFilePath.getDownloadUrl().toString());
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