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
import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.SideMenu.ProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
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

                    mExpensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(user.getUid());
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
                            holder.mMoney.setText(model.getAmount());
                            ArrayList<People> payers = model.getPayers();
                            String payerString = "";
                            for (People people : payers) {
                                payerString += people.getName() + " ";
                            }
                            holder.mReceiver.setText("Paid by: " + payerString);
                            if(model.getExpenseType().equals("food")){
                                holder.mIcon.setImageResource(R.drawable.food_icon);
                            }else if(model.getExpenseType().equals("transportation")){
                                holder.mIcon.setImageResource(R.drawable.trans_icon);
                            }else if(model.getExpenseType().equals("entertainment")){
                                holder.mIcon.setImageResource(R.drawable.ticket_icon);
                            }else if(model.getExpenseType().equals("shelter")){
                                holder.mIcon.setImageResource(R.drawable.house_icon);
                            }else if(model.getExpenseType().equals("others")){
                                holder.mIcon.setImageResource(R.drawable.other_icon);
                            }


                        }
                    };
                    mRecyclerView.setAdapter(mAdapter);

                    DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                    mDatabaseRef.child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                            if (name != null) {
                                ((TextView)findViewById(R.id.nav_text1)).setText(name);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    mDatabaseRef.child("email").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String email = dataSnapshot.getValue(String.class);
                            if (email != null) {
                                ((TextView)findViewById(R.id.nav_text2)).setText(email);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    mDatabaseRef.child("profilePhoto").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String url = dataSnapshot.getValue(String.class);
                            if (url != null) {
                                ImageView imageView = (ImageView) findViewById(R.id.nav_image);
                                Picasso.with(MainActivity.this).load(Uri.parse(url))
                                        .resize(imageView.getWidth(), imageView.getHeight())
                                        .centerInside()
                                        .into(imageView);
                                ImageView avatar = (ImageView) findViewById(R.id.avatar);
                                Picasso.with(MainActivity.this).load(Uri.parse(url))
                                        .resize(avatar.getWidth(), avatar.getHeight())
                                        .centerInside()
                                        .into(avatar);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    FirebaseDatabase.getInstance().getReference("people").child(user.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<People> members = new ArrayList<People>();
                            //People people = dataSnapshot.getValue(People.class);
                            for (DataSnapshot child : dataSnapshot.getChildren())
                                members.add(child.getValue(People.class));
                            BigDecimal pay = new BigDecimal(members.get(0).getPay());
                            BigDecimal expense = new BigDecimal(members.get(0).getExpense());
                            BigDecimal delta = pay.subtract(expense);
                            int mark = delta.compareTo(BigDecimal.ZERO);
                            if (mark == 1) {
                                ((TextView) findViewById(R.id.money_string_main)).setText("You still need to receive");
                                ((TextView) findViewById(R.id.money_main)).setText("$ " + delta.setScale(2).toString());
                            } else if (mark == -1) {
                                ((TextView) findViewById(R.id.money_string_main)).setText("You still need to pay");
                                ((TextView) findViewById(R.id.money_main)).setText("$ " + delta.negate().setScale(2).toString());
                            }
                            ((TextView) findViewById(R.id.my_expense)).setText(expense.toString());

                            for (int i = 1; i < members.size(); ++ i)
                                expense = expense.add(new BigDecimal(members.get(i).getExpense()));

                            ((TextView) findViewById(R.id.total_expense)).setText(expense.toString());
                            ((TextView) findViewById(R.id.group_number)).setText("" + members.size());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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
