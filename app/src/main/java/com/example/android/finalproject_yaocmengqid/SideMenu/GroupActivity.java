package com.example.android.finalproject_yaocmengqid.SideMenu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference peopleRef;
    private String TAG = "GroupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    database = FirebaseDatabase.getInstance();
                    peopleRef = database.getReference("people").child(user.getUid());
                    Log.d(TAG, peopleRef.toString());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }

    public void saveMember(View view) {
        EditText name = (EditText) findViewById(R.id.add_name);
        EditText email = (EditText) findViewById(R.id.add_email);

        String peopleName = name.getText().toString();
        String peopleEmail = email.getText().toString();

        People people = new People(peopleName, peopleEmail);

        peopleRef.push().setValue(people);

        Toast.makeText(GroupActivity.this, "Add member successful", Toast.LENGTH_SHORT).show();

        name.setText("");
        email.setText("");
    }
}
