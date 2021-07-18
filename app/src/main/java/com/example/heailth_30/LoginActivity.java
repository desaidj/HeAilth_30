package com.example.heailth_30;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView forgot_btn, sign_up_transfer;
    ImageView login_btn;
    EditText email, password;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init variables
        forgot_btn = findViewById(R.id.forgot_pass);
        sign_up_transfer = findViewById(R.id.sign_up_transfer);
        login_btn = findViewById(R.id.login_btn);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);

        progressBar = findViewById(R.id.progress_bar_login);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //END

        //func

        progressBar.setVisibility(View.INVISIBLE);

        //login_btn
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validator()) {
                    login();
                }
            }
        });
        //END

        //sign_up_btn
        sign_up_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
        //END

        //forgot password
        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, GetLinkActivity.class));
                finish();
            }
        });
        //END

        //END
    }

    private void login() {
        String email_t = email.getText().toString().trim();
        String password_t = password.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email_t, password_t).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Logged in successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Failed to login, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validator() {
        if (email.getText().toString().isEmpty()) {
            email.requestFocus();
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            email.requestFocus();
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.requestFocus();
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().trim().length() < 6) {
            password.requestFocus();
            Toast.makeText(this, "Password length must be greater than 6", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}