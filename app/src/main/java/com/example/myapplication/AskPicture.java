package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class AskPicture extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    public static EditText qual,gender;
    private ImageView profilePic;
    public static Uri uriProfileImage;
    FirebaseUser fuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_picture);

        Button cont = findViewById(R.id.button_continue);
        qual = findViewById(R.id.edit_education);
        gender = findViewById(R.id.gender);
        profilePic = findViewById(R.id.imageView);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(fuser.getPhotoUrl().equals("default")) {
            profilePic.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(getApplicationContext()).load(fuser.getPhotoUrl()).into(profilePic);
        }



        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskPicture.this,AskSkills.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"), CHOOSE_IMAGE);
    }
}
