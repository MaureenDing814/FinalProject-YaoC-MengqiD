package com.example.android.finalproject_yaocmengqid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.finalproject_yaocmengqid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroupActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference peopleRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        database = FirebaseDatabase.getInstance();
        peopleRef = database.getReference("people");

    }

    public void saveMember(View view) {
        EditText name = (EditText) findViewById(R.id.add_name);
        EditText email = (EditText) findViewById(R.id.add_email);

        String peopleName = name.getText().toString();
        String peopleEmail = email.getText().toString();
        double pay = 0;
        double loan = 0;

        People people = new People(peopleName, peopleEmail, pay, loan);

        peopleRef.push().setValue(people);

        Toast.makeText(GroupActivity.this, "Add member successful", Toast.LENGTH_SHORT).show();

        name.setText("");
        email.setText("");


    }
}
