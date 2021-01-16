package com.example.grpalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            user = fAuth.getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference().child("users");
            userId = user.getUid();
            final TextView fullNameTextView = (TextView) findViewById(R.id.fullName);
            final TextView emailTextView = (TextView) findViewById(R.id.email);
            final TextView phoneTextView = (TextView) findViewById(R.id.phone);
            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user userProfile = snapshot.getValue(user.class);

                    if (userProfile != null) {
                        String fullName = userProfile.fullName;
                        String email = userProfile.email;
                        String phone = userProfile.phone;

                        fullNameTextView.setText(fullName);
                        emailTextView.setText(email);
                        phoneTextView.setText(phone);


                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "something happened", Toast.LENGTH_LONG).show();
                }
            });


        }

    }

    public void addgrp(View view) {

        startActivity(new Intent(getApplicationContext(), makeup.class));



    }

    public void logout (View view ) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
