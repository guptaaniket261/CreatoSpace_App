package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AskSkills extends AppCompatActivity {
    private EditText experience;
    private EditText skills;
    private DatabaseReference userDb;
    private FirebaseUser user;
    private StorageReference imgstorageReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_skills);

        experience = findViewById(R.id.text_exp);
        skills = findViewById(R.id.text_skills);
        Button finish = findViewById(R.id.button_finish);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        imgstorageReference = FirebaseStorage.getInstance().getReference().child("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDb.child("qualification").setValue(AskPicture.qual.getText().toString());
                userDb.child("gender").setValue(AskPicture.gender.getText().toString());

                userDb.child("experience").setValue(experience.getText().toString());
                userDb.child("skills").setValue(skills.getText().toString());





//                FirebaseStorage.getInstance().getReference("Users").child(user.getUid()).child("profileImage").putFile(AskPicture.uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        userDb.child("profileImage").setValue(taskSnapshot.getStorage().getDownloadUrl().toString());
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(AskSkills.this,"Could Not Upload Image",Toast.LENGTH_SHORT).show();
//                    }
//                });

                if(uploadTask!=null && uploadTask.isInProgress())
                {
                    Toast.makeText(AskSkills.this,"upload is in progress",Toast.LENGTH_SHORT).show();
                }
                else if(AskPicture.uriProfileImage==null)
                {
                    Toast.makeText(AskSkills.this,"image not selected",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FileUpload();
                }

//                Toast.makeText(AskSkills.this, AskPicture.uriProfileImage.toString(), Toast.LENGTH_SHORT).show();




                Intent intent = new Intent(AskSkills.this,Profile.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void FileUpload()
    {
        StorageReference Ref = imgstorageReference.child(System.currentTimeMillis()+"."+getExtension(AskPicture.uriProfileImage));
        uploadTask = Ref.putFile(AskPicture.uriProfileImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        //createNewPost(imageUrl);
                                        Map imgmap = new HashMap();
                                        imgmap.put("profileImage",imageUrl);
                                        userDb.updateChildren(imgmap);
                                       // userDb.child("Image").setValue(imageUrl);

                                    }
                                });
                            }
                        }
                        Toast.makeText(AskSkills.this,"imageuploaded successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(AskSkills.this, "Could not upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private String getExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
}
