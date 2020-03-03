package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Adapter.arrayAdapter;
import com.example.myapplication.Models.cards;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.FlingCardListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class cardswiper extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference currentDb;
    private FirebaseUser currentUser;
    private String userSex,oppositeUserSex;

    // for swipecards
    private com.example.myapplication.Adapter.arrayAdapter arrayAdapter;
    private List<cards> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardswiper);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentDb = FirebaseDatabase.getInstance().getReference().child("Users");
        Toast.makeText(cardswiper.this,currentUser.getUid(),Toast.LENGTH_SHORT).show();
        checkUserSex();
        //checkUserSexFast();
        //Toast.makeText(MainActivity.this,userSex+" null 48",Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, checkUserSex()+"null", Toast.LENGTH_SHORT).show();
        //print();
        rowItems = new ArrayList<>();
        arrayAdapter = new arrayAdapter(this, R.layout.item,rowItems);
        // for swipe cards to show the images and name
        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);            // by this array adapter i am setting xml file and populating the xml file in main activity
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                cards obj = (cards) o;
                String userId = obj.getUserId();
                currentDb.child(userId).child("connection").child("nope").child(currentUser.getUid()).setValue(true);       // for opposite user sex set it to nope
                Toast.makeText(cardswiper.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                cards obj = (cards) o;
                String userId = obj.getUserId();
                currentDb.child(userId).child("connection").child("yeps").child(currentUser.getUid()).setValue(true);           // for opposite user sex set it to nope
                isConnectionMatch(userId);
                Toast.makeText(cardswiper.this, "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(cardswiper.this, "how dare you!", Toast.LENGTH_SHORT).show();
            }
        });

        //oppositeSexUser();
    }
    void checkUserSex()
    {
        //FirebaseUser user = mAuth.getCurrentUser();
        //final FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference maleDb = currentDb.child(currentUser.getUid());
        maleDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                    String gender1 = map.get("gender").toString();
                    if(gender1 != null)
                    {
                        userSex = dataSnapshot.child("gender").getValue().toString();
                        switch (userSex)
                        {
                            case "male":
                                oppositeUserSex = "female";
                                break;
                            case "female":
                                oppositeUserSex = "male";
                                break;
                        }
                        oppositeSexUser();
                        Toast.makeText(cardswiper.this, oppositeUserSex+ " abc", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Toast.makeText(MainActivity.this, userSex+"checkusersex()", Toast.LENGTH_SHORT).show();
    }

    // when the two users matched
    private void isConnectionMatch(String userId) {
        DatabaseReference currentUseDbConnection = currentDb.child(currentUser.getUid()).child("connection").child("yeps").child(userId);
        currentUseDbConnection.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentDb.child(dataSnapshot.getKey()).child("connection").child("matches").child(currentUser.getUid()).setValue(true);
                    currentDb.child(currentUser.getUid()).child("connection").child("matches").child(dataSnapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // code to print the opposite sex qualities on swipecards
    public void oppositeSexUser()
    {
        //DatabaseReference oppositeSexDb = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference userDb = currentDb.child(currentUser.getUid());
        currentDb.addChildEventListener(new ChildEventListener() {
            //&& dataSnapshot.child("gender").getValue().toString().equals(oppositeUserSex)      for future use
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && !dataSnapshot.child("connection").child("nope").hasChild(currentUser.getUid()) && !dataSnapshot.child("connection").child("yeps").hasChild(currentUser.getUid()) && !(dataSnapshot.getKey().equals(currentUser.getUid())))
                {
                    String imageUrl = "default";
                    if(!(dataSnapshot.child("profileImage").getValue().toString().equals("default")))
                    {
                       imageUrl = dataSnapshot.child("profileImage").getValue().toString();
                    }
                    cards item = new cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),imageUrl,dataSnapshot.child("age").getValue().toString(),dataSnapshot.child("qualification").getValue().toString(),dataSnapshot.child("skills").getValue().toString(),dataSnapshot.child("experience").getValue().toString());
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void checkUserSexFast()
    {
        DatabaseReference maleDb = currentDb.child("male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild(currentUser.getUid()))
                {
                    userSex = "male";
                    oppositeUserSex = "female";
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference femaleDb = currentDb.child("female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild(currentUser.getUid()))
                {
                    userSex = "female";
                    oppositeUserSex = "male";
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public void logOut(View view)
    {

        mAuth.signOut();
        Intent intent = new Intent(cardswiper.this, MainActivity.class);
        startActivity(intent);
        finish();


        Toast.makeText(cardswiper.this,"stay on this page",Toast.LENGTH_SHORT).show();
    }

    public void goToSetting(View view)
    {
        Intent intent = new Intent(cardswiper.this, Profile.class);
        intent.putExtra("gender",userSex);
        Toast.makeText(cardswiper.this, userSex+" ????", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(cardswiper.this, MatchesActivity.class);
       // intent.putExtra("gender",userSex);
        //Toast.makeText(MainActivity.this, userSex+" ????", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}

