package com.example.android.finalproject_yaocmengqid.MainContent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.finalproject_yaocmengqid.Expense;
import com.example.android.finalproject_yaocmengqid.MainContent.SummaryActivity;
import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.SideMenu.GroupActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManageGroupActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseReference peopleRef;

    private String TAG = "ManageGroupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mRecyclerView = (RecyclerView) findViewById(R.id.my_grouprecycler_view);
                    mLayoutManager = new LinearLayoutManager(ManageGroupActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setHasFixedSize(true);

                    peopleRef = FirebaseDatabase.getInstance().getReference("people").child(user.getUid());
                    Log.d(TAG, peopleRef.toString());
                    FirebaseRecyclerAdapter<People, PeopleViewHolder> mAdapter = new FirebaseRecyclerAdapter<People, PeopleViewHolder>(
                            People.class,
                            R.layout.group_item,
                            PeopleViewHolder.class,
                            peopleRef
                    ) {
                        @Override
                        protected void populateViewHolder(PeopleViewHolder holder, People people, int position) {
                            holder.memberName.setText(people.getName());
                            holder.memberEmail.setText(people.getEmail());
                            Log.d(TAG, people.toString());
                        }
                    };
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
            }
        });
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView memberName;
        public TextView memberEmail;

        public PeopleViewHolder(View v) {
            super(v);
            memberName = (TextView) v.findViewById(R.id.member_name);
            memberEmail = (TextView) v.findViewById(R.id.member_email);
        }
    }

    public void addMember(View view) {
        startActivity(new Intent(this, GroupActivity.class));
    }

}
