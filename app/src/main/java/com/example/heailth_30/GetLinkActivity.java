package com.example.heailth_30;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class GetLinkActivity extends AppCompatActivity {

    ImageView cancel_btn, get_link_btn;
    EditText email;
    ProgressBar progressBar;

    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_get_link);

        //init variables
        cancel_btn = findViewById(R.id.get_link_cancel_btn);
        get_link_btn = findViewById(R.id.get_link_btn);
        progressBar = findViewById(R.id.progress_bar_get_link);
        email = findViewById(R.id.get_link_email);
        mAuth = FirebaseAuth.getInstance();
        //END

        progressBar.setVisibility(View.INVISIBLE);

        get_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validator()){
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword();
                    finish();
                }
                else{
                    Toast.makeText(GetLinkActivity.this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetLinkActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void resetPassword(){
        String r_email = email.getText().toString().trim();

        mAuth.sendPasswordResetEmail(r_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(GetLinkActivity.this, "Link sent on your mail", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(GetLinkActivity.this, LoginActivity.class));
                }
                else{
                    Toast.makeText(GetLinkActivity.this, "Try again, something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validator(){
        if (email.getText().toString().isEmpty()){
            email.requestFocus();
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            email.requestFocus();
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
