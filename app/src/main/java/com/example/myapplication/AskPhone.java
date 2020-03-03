package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AskPhone extends AppCompatActivity implements View.OnClickListener {

     public static EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_phone);

        phone = findViewById(R.id.editText);
        Button cont = findViewById(R.id.button_continue);

        cont.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_continue) {
            startActivity(new Intent(AskPhone.this, SignUp.class));
        }
    }
}
