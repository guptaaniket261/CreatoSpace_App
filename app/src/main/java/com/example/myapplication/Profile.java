package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    StorageReference mStorageRef;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    TextView first, phone, dob, qualification, experience, skills;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button edit = findViewById(R.id.edit);
        Button logout = findViewById(R.id.logout);
        Button chat = findViewById(R.id.chat);
        Button cardswiper = findViewById(R.id.card_swiper);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(Profile.this, MainActivity.class);
            startActivity(intent);
            finish();
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

        //final File file = File.createTempFile("test1Disha", "jpg");

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loadPost:onCancelled", databaseError.toException().toString());
            }
        });


//            mStorageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap user = BitmapFactory.decodeFile(file.getAbsolutePath());
//                    circularImageView.setImageBitmap(user);
//                }
//            });


// Set Color
//        circularImageView.setCircleColor(Color.WHITE);
//// or with gradient
//        circularImageView.setCircleColorStart(Color.BLACK);
//        circularImageView.setCircleColorEnd(Color.RED);
//        circularImageView.setCircleColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);
//
//// Set Border
//        circularImageView.setBorderWidth(3f);
//        circularImageView.setBorderColor(Color.BLACK);
//// or with gradient
//        circularImageView.setBorderColorStart(Color.BLACK);
//        circularImageView.setBorderColorEnd(Color.RED);
//        circularImageView.setBorderColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);
//
//// Add Shadow with default param
//        circularImageView.setShadowEnable(true);
//// or with custom param
//        circularImageView.setShadowRadius(6f);
//        circularImageView.setShadowColor(Color.RED);
//        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,Chat.class);
                startActivity(intent);
                finish();
            }
        });

        cardswiper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,cardswiper.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
