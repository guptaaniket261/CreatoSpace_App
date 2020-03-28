package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
    }
    Intent intent = getIntent();


    public void timeline(View view){
        Intent intent = new Intent(this, tl.class);
        startActivity(intent);
    }
    public void features(View view){
        Intent intent = new Intent(this, fe.class);
        startActivity(intent);
    }
    public void working(View view){
        Intent intent = new Intent(this, wo.class);
        startActivity(intent);
    }
    public void purchase(View view){
        Intent intent = new Intent(this, pu.class);
        startActivity(intent);
    }
    public void contact(View view){
        Intent intent = new Intent(this, co.class);
        startActivity(intent);
    }
}
