package com.example.android.finalproject_yaocmengqid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android.finalproject_yaocmengqid.R;

public class GroupActivity extends AppCompatActivity {
    public String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    public void saveMember(View view){
        EditText name = (EditText)findViewById(R.id.add_name);
        EditText email = (EditText)findViewById(R.id.add_email);


        String peopleName = name.getText().toString();
        String peopleEmail= email.getText().toString();
        double pay = 0;
        double loan = 0;


        People people = new People(peopleName,peopleEmail,pay,loan,uid);




}
