package com.gil_shiran_or.keepon.login_register;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mEmailText;
    private Button mResetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mEmailText = (EditText)findViewById(R.id.reset_mail);
        mResetPassword = (Button)findViewById(R.id.resetPasswordBtn);
        mAuth = FirebaseAuth.getInstance();
    }

    public void resetPasswordClicked(View v)
    {
        String useremail = mEmailText.getText().toString().trim();

        if(useremail.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please enter your registered email ID", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void backToSignIn(View v)
    {
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
