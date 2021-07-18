package com.example.heailth_30;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    ImageView save_btn, password_reset_btn, profile_pic;
    EditText name, age, email;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String userID;

    private Uri imageUri;
    private String userUri = "";
    //private StorageTask uploadTask;
    //private StorageReference storageReference;

    ProgressBar progressBar;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        name = v.findViewById(R.id.profile_name);
        age = v.findViewById(R.id.profile_age);
        email = v.findViewById(R.id.profile_email);
        save_btn = v.findViewById(R.id.profile_save_btn);
        password_reset_btn = v.findViewById(R.id.profile_password_edit);
        mAuth = FirebaseAuth.getInstance();
        profile_pic = v.findViewById(R.id.profile_pic);
        progressBar = v.findViewById(R.id.progress_bar_profile);

        progressBar.setVisibility(View.INVISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        userID = user.getUid();
        //storageReference = FirebaseStorage.getInstance().getReference().child("Profile_pic");

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User_info userProfile = snapshot.getValue(User_info.class);
                if (userProfile != null) {
                    name.setText(userProfile.name);
                    age.setText(userProfile.age + " yrs");
                    email.setText(userProfile.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        password_reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }

            private void resetPassword() {
                String r_email = email.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(r_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Reset link send to your mail", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Try again, something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // This gives an exception when you click on save button, due to line 119
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //uploadProfilePic();
                
                String r_name = name.getText().toString().trim();
                String r_age = age.getText().toString().trim();
                String r_email = email.getText().toString().trim();

                HashMap<String, Object> hashMap = new HashMap<String, Object>();

                hashMap.put("name", name);
                hashMap.put("age", age);
                hashMap.put("email", email);

                reference.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        getFragmentManager().beginTransaction().replace(R.id.container, new MenuFragment()).commit();
                        Toast.makeText(getActivity(), "Profile saved successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //getUserInfo();

        return v;
    }

    /*private void getUserInfo() {
        reference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0){
                    String image = snapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(profile_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && requestCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profile_pic.setImageURI(imageUri);
        }
        else{
            Toast.makeText(getActivity(), "Error, try again", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*private void uploadProfilePic() {
        progressBar.setVisibility(View.VISIBLE);

        if(imageUri != null){
            final StorageReference fileRef = storageReference.child(mAuth.getCurrentUser().getUid() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri =task.getResult();
                        userUri = downloadUri.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", userUri);

                        reference.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Image not selected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/
}
