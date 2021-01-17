package com.example.grpalarm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
public class makeup extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private EditText grptitle;
    private EditText grpdesc;
    private Button creategrpbtn;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup);

        //initializing the UI views
        grptitle = findViewById(R.id.grouptitle);
        grpdesc = findViewById(R.id.groupdesc);
        creategrpbtn = findViewById(R.id.groupbutton);


        fAuth = FirebaseAuth.getInstance();

        //click to create group

        creategrpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createthegrp();

            }
        });









    }

    private void createthegrp() {

        String groupTitle =  grptitle.getText().toString().trim();
        String groupDescription = grpdesc.getText().toString().trim();
        //check if entered text is empty
        if(TextUtils.isEmpty(groupTitle)) {
            Toast.makeText(this, "please enter group titile", Toast.LENGTH_SHORT).show();
            return;
        }
        String g_timestamp = ""+System.currentTimeMillis();
        createGroup(
                ""+g_timestamp,
                ""+groupTitle,
                ""+groupDescription


        );


    }

    private void createGroup(final String g_timestamp, final String groupTitle, String groupDescription) {

        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("groupId","" + g_timestamp);
        hashMap.put("groupTitle","" + groupTitle);
        hashMap.put("groupDescription","" + groupDescription);
        hashMap.put("CreatedBy", "" + fAuth.getUid());

        //create group

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(g_timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HashMap<String,String> hashMap1 = new HashMap<>();
                hashMap1.put("uid", fAuth.getUid());
                hashMap1.put("role:", "creator");

                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups");
                ref1.child(g_timestamp).child("participants").child(fAuth.getUid()).setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(makeup.this,"Group created successfully...", Toast.LENGTH_SHORT).show();

                    }
                });
                HashMap<String,Object> hashMap2 = new HashMap<>();
                hashMap2.put("groupTitle","" + groupTitle);

                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("users");
                ref2.child(fAuth.getUid()).child("Groups").child(g_timestamp).setValue(hashMap2);

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(makeup.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });





    }
}