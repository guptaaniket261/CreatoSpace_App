package com.example.myapplication.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.AskPicture;
import com.example.myapplication.AskSkills;
import com.example.myapplication.R;
import com.example.myapplication.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bolts.Continuation;
import bolts.Task;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    CircleImageView image_profile;
    TextView username;
    TextView phone;
    TextView dob;
    TextView qualification;
    EditText usernameEdit;
    EditText phone_edit;
    EditText dob_edit;
    EditText qualification_edit;
    Button done;

    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;
    private DatabaseReference userDb;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        usernameEdit = view.findViewById(R.id.username_edit);
        phone = view.findViewById(R.id.phone);
        phone_edit = view.findViewById(R.id.phone_edit);
        dob = view.findViewById(R.id.dob);
        dob_edit = view.findViewById(R.id.dob_edit);
        qualification = view.findViewById(R.id.qualifiaction);
        qualification_edit = view.findViewById(R.id.qualifiaction_edit);
        done = view.findViewById(R.id.done);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        storageReference = FirebaseStorage.getInstance().getReference("Images").child(fuser.getUid());
        userDb = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                username.setText(user.getName());
                usernameEdit.setText(user.getName());
                phone.setText(user.getPhone());
                phone_edit.setText(user.getPhone());
                dob.setText(user.getDob());
                dob_edit.setText(user.getDob());
                qualification.setText(user.getQualification());
                qualification_edit.setText(user.getQualification());

                if(user.getProfileImage().equals("default")) {
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getContext()).load(user.getProfileImage()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setVisibility(View.GONE);
                usernameEdit.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setVisibility(View.GONE);
                phone_edit.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dob.setVisibility(View.GONE);
                dob_edit.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qualification.setVisibility(View.GONE);
                qualification_edit.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap();
                map.put("name",usernameEdit.getText().toString());
                map.put("phone",phone_edit.getText().toString());
                map.put("dob",dob_edit.getText().toString());
                map.put("qualification",qualification_edit.getText().toString());
                userDb.updateChildren(map);
                username.setText(usernameEdit.getText().toString());
                username.setVisibility(View.VISIBLE);
                usernameEdit.setVisibility(View.GONE);
                phone.setText(phone_edit.getText().toString());
                phone.setVisibility(View.VISIBLE);
                phone_edit.setVisibility(View.GONE);
                dob.setText(dob_edit.getText().toString());
                dob.setVisibility(View.VISIBLE);
                dob_edit.setVisibility(View.GONE);
                qualification.setText(qualification_edit.getText().toString());
                qualification.setVisibility(View.VISIBLE);
                qualification_edit.setVisibility(View.GONE);
                done.setVisibility(View.GONE);

            }
        });

        return view;
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    private void FileUpload()
    {
        StorageReference Ref = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
        uploadTask = Ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                com.google.android.gms.tasks.Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
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
                        Toast.makeText(getContext(),"imageuploaded successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Could not upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && resultCode==-1 && data!=null && data.getData()!=null) {
            imageUri = data.getData();

            if(uploadTask!=null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in Progress", Toast.LENGTH_SHORT).show();
            }

            FileUpload();
        }
    }
}
