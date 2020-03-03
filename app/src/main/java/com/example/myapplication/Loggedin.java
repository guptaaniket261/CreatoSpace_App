package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Loggedin extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Button logOut;
    private Button profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
        firebaseAuth = FirebaseAuth.getInstance();
        logOut = findViewById(R.id.logout);
        profile = findViewById(R.id.button_profile);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Loggedin.this,MainActivity.class);
                finish();
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loggedin.this, Profile.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
