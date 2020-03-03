
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AskDOB extends AppCompatActivity {
    public static EditText day,month,year;//public static variables to acess these variables at in other activities. this makes our job easy but don't know is this secure?
    private Button toMail;
    private Button toName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_dob);

        toMail = findViewById(R.id.Next);
        toName = findViewById(R.id.Back);
        day = findViewById(R.id.day);
        month = findViewById((R.id.month));
        year = findViewById(R.id.year);

        toName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//when back button is clicked it sets all fields to emoty and finish this activity and opens previous activity Askname
                day.setText("");//setting fields to empty
                month.setText("");
                year.setText("");
                AskDOB.this.finish();
                Intent intent = new Intent(AskDOB.this, Askname.class);
                startActivity(intent);
            }
        });

        toMail.setOnClickListener(new View.OnClickListener() {//when next button is clicked it puts all the details entered into fields into public static valiables
            @Override                                         //which can be acessed by another activity outside this also we don't finish this activity as we will nedd this data while registering
            public void onClick(View v) {
                day = findViewById(R.id.day);//getting data from fields (EditText)
                month = findViewById(R.id.month);
                year = findViewById(R.id.year);

                Intent intent = new Intent(AskDOB.this, AskEmail.class);//creating a new intent pointing to AskEmail
                startActivity(intent);//starting this new intent
            }
        });



    }
}