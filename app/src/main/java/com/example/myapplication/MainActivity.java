package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private TextView error;
    private FirebaseAuth firebaseLogin;
    private ProgressDialog progress;
    private DatabaseReference userDb;
    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;


    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = findViewById(R.id.Userid);
        password = findViewById(R.id.Password);
        error = findViewById(R.id.iferror);
        Button login = findViewById(R.id.Login);
        TextView signup = findViewById(R.id.signup);
        firebaseLogin = FirebaseAuth.getInstance();
        userDb = FirebaseDatabase.getInstance().getReference("Users");


        progress = new ProgressDialog(this);//progression bar


        login.setOnClickListener(new View.OnClickListener() {//when user clicks on login button
            @Override
            public void onClick(View v) {
                if (fields()) {
                    validate(username.getText().toString(), password.getText().toString());
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {//if user wants to register
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Askname.class);
                startActivity(intent);

            }
        });


        //google button sign after this


        //first we intialized the FirebaseAuth object
        firebaseLogin = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.google_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (firebaseLogin.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Profile.class));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        firebaseLogin.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseLogin.getCurrentUser();
                            finish();

                            assert user != null;
                            userDb.child(user.getUid()).child("gender").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue()==null) {
                                        FirebaseUser user = firebaseLogin.getCurrentUser();
                                        assert user != null;
                                        Users user1 = new Users(user.getUid(),user.getDisplayName(),user.getEmail(),user.getPhoneNumber(),null,user.getPhotoUrl().toString(),null,null,null,null);
                                        userDb.child(user.getUid()).setValue(user1);

//                                        StorageReference Ref = FirebaseStorage.getInstance().getReference().child(("Images")).child(System.currentTimeMillis()+"."+getExtension(AskPicture.uriProfileImage));
//                                        uploadTask = Ref.putFile(user.getPhotoUrl())
//                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                    @Override
//                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                        // Get a URL to the uploaded content
//                                                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                                                        if (taskSnapshot.getMetadata() != null) {
//                                                            if (taskSnapshot.getMetadata().getReference() != null) {
//                                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                    @Override
//                                                                    public void onSuccess(Uri uri) {
//                                                                        String imageUrl = uri.toString();
//                                                                        //createNewPost(imageUrl);
//                                                                        Map imgmap = new HashMap();
//                                                                        imgmap.put("profileImage",imageUrl);
//                                                                        userDb.updateChildren(imgmap);
//                                                                        // userDb.child("Image").setValue(imageUrl);
//
//                                                                    }
//                                                                });
//                                                            }
//                                                        }
//                                                        Toast.makeText(MainActivity.this,"image uploaded successfully",Toast.LENGTH_SHORT).show();
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception exception) {
//                                                        Toast.makeText(MainActivity.this, "Could not upload image", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });


                                        Intent intent = new Intent(MainActivity.this,AskPicture.class);//creating a new intent pointing to Profile
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);//starting this new intent
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, Profile.class);//creating a new intent pointing to AskPicture
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);//starting this new intent
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void validate(String userName, String Password) {//this function validate user with Firebase database
        progress.setMessage("Authenticating");
        progress.show();
        firebaseLogin.signInWithEmailAndPassword(userName, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progress.dismiss();
                    finish();
                    userDb.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child("gender").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue()==null) {
                                Intent intent = new Intent(MainActivity.this, AskPicture.class);//creating a new intent pointing to Profile
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);//starting this new intent
                            } else {
                                Intent intent = new Intent(MainActivity.this, Profile.class);//creating a new intent pointing to AskPicture
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);//starting this new intent
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    progress.dismiss();
                    error.setText("Wrong Username or Password!");
                    Toast.makeText(MainActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public boolean fields() {
        boolean valid = true;
        if (username.getText().toString().isEmpty() || username.length() < 3) {
            username.setError("at least 3 characters");
            valid = false;
        } else {
            username.setError(null);
        }


        if (password.getText().toString().isEmpty() || password.length() < 6 || password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

    private String getExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

}



