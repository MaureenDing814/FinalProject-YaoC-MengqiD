package com.example.android.finalproject_yaocmengqid.MainContent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.finalproject_yaocmengqid.Expense;
import com.example.android.finalproject_yaocmengqid.LoginActivity;
import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.SideMenu.GroupActivity;
import com.example.android.finalproject_yaocmengqid.SideMenu.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.android.finalproject_yaocmengqid.R.id.help;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mExpensesRef;
    private String TAG = "LoginActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    mRecyclerView = (RecyclerView) findViewById(R.id.my_mainrecycler_view);

                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    mRecyclerView.setHasFixedSize(true);

                    // use a linear layout manager
                    mLayoutManager = new LinearLayoutManager(MainActivity.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                    mExpensesRef = FirebaseDatabase.getInstance().getReference(user.getUid()).child("expenses");
                    FirebaseRecyclerAdapter<Expense, ExpenseViewHolder> mAdapter = new FirebaseRecyclerAdapter<Expense, ExpenseViewHolder>(
                            Expense.class,
                            R.layout.main_item,
                            ExpenseViewHolder.class,
                            mExpensesRef
                    ) {
                        @Override
                        protected void populateViewHolder(ExpenseViewHolder holder, Expense model, int position) {
                            holder.mName.setText(model.getExpenseType());
                            holder.mDate.setText(model.getDateString());
                            holder.mMoney.setText(Double.toString(model.getAmount()));
                            ArrayList<People> payers = model.getPayers();
                            String payerString = "";
                            for (People people : payers) {
                                payerString += people.getName() + " ";
                            }
                            holder.mReceiver.setText("Paid by: " + payerString);

                            holder.mIcon.setImageResource(R.drawable.common_full_open_on_phone);
                        }
                    };
                    mRecyclerView.setAdapter(mAdapter);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                // ...
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void manageGroup(View view) {
        startActivity(new Intent(this, ManageGroupActivity.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String [] emailList =  {"dz2276@tc.columbia.edu"};
        if (id == R.id.setting) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.manage_group) {
            startActivity(new Intent(this, ManageGroupActivity.class));

        } else if (id == R.id.summary) {
            startActivity(new Intent(this, SummaryActivity.class));

        } else if (id == help) {
            Intent help = new Intent(Intent.ACTION_SENDTO);
            help.setData(Uri.parse("mailto:"));
            help.putExtra(Intent.EXTRA_EMAIL, emailList );
            help.putExtra(Intent.EXTRA_SUBJECT, "Question");
            if (help.resolveActivity(getPackageManager())!=null) {
                startActivity(help);
            }
        } else if (id == R.id.log_out) {
            logout(item);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(MenuItem item) {
        mAuth.signOut();
    }

    public void groupSummary(MenuItem item) {
        startActivity(new Intent(this, SummaryActivity.class));
    }

    public void calculate(View view) {
        startActivity(new Intent(this, CalculateActivity.class));
    }

    private static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mIcon;
        public TextView mName;
        public TextView mDate;
        public TextView mMoney;
        public TextView mReceiver;
        public View myView;

        public ExpenseViewHolder(View v) {
            super(v);
            mIcon = (ImageView) v.findViewById(R.id.icon);
            mName = (TextView) v.findViewById(R.id.item_name);
            mDate = (TextView) v.findViewById(R.id.date);
            mMoney = (TextView) v.findViewById(R.id.money);
            mReceiver = (TextView) v.findViewById(R.id.receiver_name);
            myView = v;
        }
    }

}
