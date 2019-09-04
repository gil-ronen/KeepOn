package com.gil_shiran_or.keepon.login_register;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gil_shiran_or.keepon.HomeActivity;
import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.nav.TraineeNavActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.login_mail);
        mPasswordView = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.loginBtn);
        loginProgress = findViewById(R.id.login_progress);
        loginProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance(); // Grab an instance of FirebaseAuth
        mUser = mAuth.getCurrentUser();
    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {

        attemptLogin();

    }

    public void forgotPasswordClicked(View v)
    {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    private void attemptLogin() {

        loginProgress.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(this, "Please Verify All Field", Toast.LENGTH_SHORT).show();
            btnLogin.setVisibility(View.VISIBLE);
            loginProgress.setVisibility(View.INVISIBLE);
            return;
        }
        Toast.makeText(this, "Login in progress...", Toast.LENGTH_SHORT).show();

        // Use FirebaseAuth to sign in with email & password
         mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d("KeepOn", "signInWithEmail() onComplete: " + task.isSuccessful());

                if(!task.isSuccessful())
                {
                    Log.d("KeepOn", "Problem signing in: " + task.getException());
                    showErrorDialog("There was a problem signing in");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);

                    mAuth = FirebaseAuth.getInstance(); // Grab an instance of FirebaseAuth
                    mUser = mAuth.getCurrentUser();

                    String user_id = mUser.getUid();

                    query = FirebaseDatabase.getInstance().getReference().child("Users").child("Trainees")
                            .orderByKey()
                            .equalTo(user_id);
                    query.addValueEventListener(valueEventListener);
                }

            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Intent intent;

            if (dataSnapshot.exists()) //the user is connected as Trainee
            {
                intent = new Intent(getApplicationContext(), TraineeNavActivity.class);
            }
            else //the user is connected as Trainer
            {
                intent = new Intent(getApplicationContext(), HomeActivity.class);
            }

            query.removeEventListener(this);
            startActivity(intent);
            finish();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    // Show error on screen with an alert dialog
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