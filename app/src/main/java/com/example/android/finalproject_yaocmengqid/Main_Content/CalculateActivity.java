package com.example.android.finalproject_yaocmengqid.Main_Content;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.finalproject_yaocmengqid.Plan;
import com.example.android.finalproject_yaocmengqid.R;

import java.util.ArrayList;

public class CalculateActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList <Plan> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        results = new ArrayList<Plan>();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CalculateAdapter(results);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void share(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bill Summary");
        intent.putExtra(Intent.EXTRA_TEXT, "You still need to pay");
        if (intent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(intent);
        }

    }

}
