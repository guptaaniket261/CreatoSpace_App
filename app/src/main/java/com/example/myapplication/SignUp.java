package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        findViewById(R.id.btnSignup).setOnClickListener(this);


    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(this, "U Signed In successfully", Toast.LENGTH_LONG).show();
            String phoneNumber = AskPhone.phone.getText().toString();
            String first = Askname.firstName.getText().toString();
            String last = Askname.lastName.getText().toString();
            String mail = AskEmail.email.getText().toString();
            String dob = AskDOB.day + "/" + AskDOB.month + "/" + AskDOB.year;
            Users user1 = new Users(user.getUid(),first+" "+last,mail,phoneNumber,dob,"default",null,null,null,null);
            userDb.child(user.getUid()).setValue(user1);


            Intent intent = new Intent(SignUp.this, AskPicture.class);//creating a new intent pointing to Profile
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);//starting this new intent

        } else {
            Log.w("SignUp", "createUserWithEmail:failure");
            Toast.makeText(SignUp.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        EditText password = findViewById(R.id.Pwd);
        EditText password2 = findViewById(R.id.PwdRe);
        firebaseAuth = FirebaseAuth.getInstance();
        userDb = FirebaseDatabase.getInstance().getReference("Users");

        if (password.getText().toString().equals(password2.getText().toString())) {
            if (password.getText().toString().isEmpty() || password.length() < 6 || password.length() > 10) {
                password.setError("between 6 and 10 alphanumeric characters");

            } else {
                password.setError(null);
                String pass = password.getText().toString();
                String mail = AskEmail.email.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SignUp", "createUserWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    updateUI(user);


                                } else {

                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(getApplicationContext(), "You are already registered!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    // If sign in fails, display a message to the user.
                                    Log.w("SignUp", "createUserWithEmail:failure", task.getException());
                                    updateUI(null);
                                }

                                // ...
                            }
                        });




            }
        } else {
            password.setError("Passwords do not match.");
            password2.setError("Passwords do not match.");
        }
    }

}