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
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, password, age;
    ImageView signup_btn;
    TextView login_transfer;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //init variables
        name = findViewById(R.id.sign_up_name);
        email = findViewById(R.id.sign_up_email);
        age = findViewById(R.id.sign_up_age);
        password = findViewById(R.id.sign_up_pass);
        signup_btn = findViewById(R.id.sign_up_btn);
        login_transfer = findViewById(R.id.signup_to_login_transfer);
        progressBar = findViewById(R.id.progress_bar_signup);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //END

        progressBar.setVisibility(View.INVISIBLE);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up();
            }
        });

        login_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    private void sign_up() {
        String i_name = name.getText().toString().trim();
        String i_email = email.getText().toString().trim();
        String i_age = age.getText().toString().trim();
        String i_password = password.getText().toString().trim();

        if (validator()) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(i_email, i_password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User_info user_info = new User_info(i_name, i_email, i_age);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user_info).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(SignupActivity.this, "Signed up successfully", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            finish();
                                            /*
                                            if (user.isEmailVerified()) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(SignupActivity.this, "Signed up successfully", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                finish();
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                user.sendEmailVerification();
                                                Toast.makeText(SignupActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                finish();
                                            }*/
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Failed to register, try again", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignupActivity.this, "Failed to register, try again", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private boolean validator() {
        if (name.getText().toString().isEmpty()) {
            name.requestFocus();
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            email.requestFocus();
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
            email.requestFocus();
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (age.getText().toString().isEmpty()) {
            age.requestFocus();
            Toast.makeText(this, "Please enter your age", Toast.LENGTH_LONG).show();
            return false;
        }
        if (age.getText().toString().length() > 3) {
            age.requestFocus();
            Toast.makeText(this, "Please enter valid age", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.requestFocus();
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.getText().toString().length() < 6) {
            password.requestFocus();
            Toast.makeText(this, "Password must be atleast 6 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}