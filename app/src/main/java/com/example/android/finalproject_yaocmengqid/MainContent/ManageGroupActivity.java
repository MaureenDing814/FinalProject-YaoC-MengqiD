package com.example.android.finalproject_yaocmengqid.MainContent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.finalproject_yaocmengqid.MainContent.SummaryActivity;
import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.SideMenu.GroupActivity;

public class ManageGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);
    }

        public void edit(View view)
    {
        startActivity(new Intent(this, GroupActivity.class));
    }

}
