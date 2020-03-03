package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfile extends AppCompatActivity {
    private Button done;

    StorageReference mStorageRef;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    TextView first, phone, dob, qualification, experience, skills, gender;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        done = findViewById(R.id.done);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(EditProfile.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        //final CircularImageView circularImageView = findViewById(R.id.circularImageView);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Users").child(user.getUid());

        first = findViewById(R.id.Firstname);
        phone = findViewById(R.id.Phone);
        dob = findViewById(R.id.DOB);
        qualification = findViewById(R.id.qualifiaction);
        experience = findViewById(R.id.experience);
        skills = findViewById(R.id.skills);
        gender = findViewById(R.id.gender);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                first.setText(user != null && user.getName() != null ? user.getName() : "");
                assert user != null;
                if (user.getPhone() == null) {
                    phone.setText("");
                } else {
                    phone.setText(user.getPhone());
                }
                //phone.setText((user!=null && user.getPhone() != null) ? user.getPhone() : "");
                dob.setText(user.getDob() != null ? user.getDob() : "");
                qualification.setText(user.getQualification() != null ? user.getQualification() : "");
                experience.setText(user.getExperience() != null ? user.getExperience() : "");
                skills.setText(user.getSkills() != null ? user.getSkills() : "");
                gender.setText(user.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loadPost:onCancelled", databaseError.toException().toString());
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user1 = new Users(user.getUid(), first.getText().toString(), user.getEmail(), phone.getText().toString(), dob.getText().toString(), "default", qualification.getText().toString(), experience.getText().toString(), skills.getText().toString(),gender.getText().toString());
                mRef.setValue(user1);
                Intent intent = new Intent(EditProfile.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
