package com.gil_shiran_or.keepon;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private ProgressBar mLoadingProgress;
    private Button mRegBtn;
    RadioGroup mRadioUserTypeGroup;
    RadioButton mRadioUserTypeButton;

    private FirebaseAuth mAuth; // Firebase instance variables
    //private DatabaseReference mDatabase;
    private boolean mIsTrainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mConfirmPasswordView =findViewById(R.id.register_confirm_password);
        mUsernameView = findViewById(R.id.register_username);
        mLoadingProgress = findViewById(R.id.regProgressBar);
        mRegBtn = findViewById(R.id.register_sign_up_button);
        mRadioUserTypeGroup = findViewById(R.id.register_user_type_radioGroup);
        mIsTrainer = false;

        mLoadingProgress.setVisibility(View.INVISIBLE);

        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance(); // Get hold of an instance of FirebaseAuth
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v)
    {
        attemptRegistration();
    }

    public void TraineeOrTrainerButton(View v)
    {
        int radioId = mRadioUserTypeGroup.getCheckedRadioButtonId();
        mRadioUserTypeButton = findViewById(radioId);
        if(mRadioUserTypeButton.getText().toString().equals("Trainee"))
        {
            Log.d("KeepOn", "Trainee is Checked");
            mIsTrainer = false;
        }
        else if(mRadioUserTypeButton.getText().toString().equals("Trainer"))
        {
            Log.d("KeepOn", "Trainer is Checked");
            mIsTrainer = true;
        }
    }

    public void backToSignIn(View v)
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void attemptRegistration() {

        mRegBtn.setVisibility(View.INVISIBLE);
        mLoadingProgress.setVisibility(View.VISIBLE);

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mUsernameView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String username = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            mRegBtn.setVisibility(View.VISIBLE);
            mLoadingProgress.setVisibility(View.INVISIBLE);
            focusView.requestFocus();
        }
        else {
            //createFirebaseUser();
            checkIfEmailExistInDB();

        }
    }

    private boolean isEmailValid(String email) {
        // add more checking logic here.
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        // add more logic to check for a valid password (minimum 6 characters)
        String confirmPassword = mConfirmPasswordView.getText().toString();
        return confirmPassword.equals(password) && password.length() > 7;
    }

    private void checkIfEmailExistInDB()
    {
        mEmailView.setError(null);
        final View focusView = mEmailView;

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String username = mUsernameView.getText().toString();


        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                boolean isEmailExist = !task.getResult().getSignInMethods().isEmpty();

                mRegBtn.setVisibility(View.VISIBLE);
                mLoadingProgress.setVisibility(View.INVISIBLE);

                if(!isEmailExist)
                {
                    Intent intent;
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);
                    bundle.putString("password", password);
                    bundle.putString("username", username);

                    if(mIsTrainer)
                    {
                        bundle.putString("userType", "Trainer");
                        intent = new Intent(getApplicationContext(), TrainerDetailsRegister.class);
                    }
                    else
                    {
                        bundle.putString("userType", "Trainee");
                        intent = new Intent(getApplicationContext(), TraineeDetailsRegister.class);
                    }

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    mEmailView.setError("This email already exist");
                    focusView.requestFocus();
                    showErrorDialog("Another user has already signed up with this email.\n" +
                            "Please register with another email.");
                }
            }
        });
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
}
