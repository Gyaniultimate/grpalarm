package com.example.grpalarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void logout (View view ) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
