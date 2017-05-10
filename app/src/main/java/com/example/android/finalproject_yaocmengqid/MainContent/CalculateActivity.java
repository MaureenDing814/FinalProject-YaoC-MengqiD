package com.example.android.finalproject_yaocmengqid.MainContent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.finalproject_yaocmengqid.Expense;
import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.Plan;
import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.SideMenu.ProfileActivity;
import com.example.android.finalproject_yaocmengqid.Utils.CalculateAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class CalculateActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView photo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //private People user;

    private ArrayList<Plan> results;

    private String TAG = "CalculateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        results = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    photo = (ImageView) findViewById(R.id.calculate_photo);

                    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

                    mDatabaseRef.child("profilePhoto").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String url = dataSnapshot.getValue(String.class);
                            if (url != null) {

                                Picasso.with(CalculateActivity.this).load(Uri.parse(url))
                                        .resize(photo.getWidth(), photo.getHeight())
                                        .centerInside()
                                        .into(photo);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(CalculateActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

        };
        mAuth.addAuthStateListener(mAuthListener);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(CalculateActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CalculateAdapter(results);
        mRecyclerView.setAdapter(mAdapter);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("people").child(user.getUid());
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Map<String, Object> collection = (Map<String, Object>)dataSnapshot.getValue();
                            ArrayList<People> members = new ArrayList<People>();
                            for (DataSnapshot child : dataSnapshot.getChildren())
                                members.add(child.getValue(People.class));
                            BigDecimal pay = new BigDecimal(members.get(0).getPay());
                            BigDecimal expense = new BigDecimal(members.get(0).getExpense());
                            BigDecimal delta = pay.subtract(expense);
                            int mark = delta.compareTo(BigDecimal.ZERO);
                            if (mark == 1) {
                                ((TextView) findViewById(R.id.cal_pay_recv)).setText("You still need to receive");
                                ((TextView) findViewById(R.id.calculate)).setText("$ " + delta.setScale(2).toString());
                            } else if (mark == -1) {
                                ((TextView) findViewById(R.id.cal_pay_recv)).setText("You still need to pay");
                                ((TextView) findViewById(R.id.calculate)).setText("$ " + delta.negate().setScale(2).toString());
                            }

                            for (int i = 1; i < members.size(); ++ i) {
                                pay = new BigDecimal(members.get(i).getPay());
                                expense = new BigDecimal(members.get(i).getExpense());
                                delta = pay.subtract(expense);
                                mark = delta.compareTo(BigDecimal.ZERO);
                                if (mark == 1) {
                                    results.add(new Plan(members.get(0).getName(), delta.toString(), members.get(i).getName()));
                                } else if (mark == -1) {
                                    results.add(new Plan(members.get(i).getName(), delta.negate().toString(), members.get(0).getName()));
                                }
                            }

                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    finish();
                }
            }
        });
    }

    public void share(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bill Summary");
        intent.putExtra(Intent.EXTRA_TEXT, "You still need to pay");
        if (intent.resolveActivity(getPackageManager())!=null) {
            startActivity(intent);
        }

    }
}
