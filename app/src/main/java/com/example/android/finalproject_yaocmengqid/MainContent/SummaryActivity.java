package com.example.android.finalproject_yaocmengqid.MainContent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.android.finalproject_yaocmengqid.Expense;
import com.example.android.finalproject_yaocmengqid.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    FirebaseDatabase.getInstance().getReference("expenses").child(user.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    BigDecimal foodExpense = new BigDecimal(0);
                                    BigDecimal transExpense = new BigDecimal(0);
                                    BigDecimal entExpense = new BigDecimal(0);
                                    BigDecimal shelExpense = new BigDecimal(0);
                                    BigDecimal otherExpense = new BigDecimal(0);
//                                    ArrayList<Expense> expenses = new ArrayList<Expense>();
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        if (child.getValue(Expense.class).getExpenseType().equals("food")) {
                                            foodExpense = foodExpense.add(new BigDecimal(child.getValue(Expense.class).getAmount()));
                                            ((TextView) findViewById(R.id.food_money)).setText(foodExpense.toString());
                                        } else if (child.getValue(Expense.class).getExpenseType().equals("transportation")) {
                                            transExpense = transExpense.add(new BigDecimal(child.getValue(Expense.class).getAmount()));
                                            ((TextView) findViewById(R.id.trans_money)).setText(transExpense.toString());
                                        } else if (child.getValue(Expense.class).getExpenseType().equals("entertainment")) {
                                            entExpense = entExpense.add(new BigDecimal(child.getValue(Expense.class).getAmount()));
                                            ((TextView) findViewById(R.id.entertainment_money)).setText(entExpense.toString());
                                        } else if (child.getValue(Expense.class).getExpenseType().equals("shelter")) {
                                            shelExpense = shelExpense.add(new BigDecimal(child.getValue(Expense.class).getAmount()));
                                            ((TextView) findViewById(R.id.house_money)).setText(shelExpense.toString());
                                        } else if (child.getValue(Expense.class).getExpenseType().equals("others")) {
                                            otherExpense = otherExpense.add(new BigDecimal(child.getValue(Expense.class).getAmount()));
                                            ((TextView) findViewById(R.id.other_money)).setText(otherExpense.toString());
                                        }
                                    }

                                    //Drawing chart
                                    BigDecimal totalExpense = foodExpense.add(transExpense).add(entExpense).add(shelExpense).add(otherExpense);
                                    float[] data = new float[5];
                                    data[0] = foodExpense.divide(totalExpense, 8, BigDecimal.ROUND_HALF_UP).floatValue();
                                    data[1] = transExpense.divide(totalExpense, 8, BigDecimal.ROUND_HALF_UP).floatValue();
                                    data[2] = shelExpense.divide(totalExpense, 8, BigDecimal.ROUND_HALF_UP).floatValue();
                                    data[3] = entExpense.divide(totalExpense, 8, BigDecimal.ROUND_HALF_UP).floatValue();
                                    data[4] = otherExpense.divide(totalExpense, 8, BigDecimal.ROUND_HALF_UP).floatValue();
                                    ArrayList<PieEntry> entries = new ArrayList<>();
                                    ArrayList<Integer> colors = new ArrayList<Integer>();
                                    if (data[0] > 1e-6) {
                                        entries.add(new PieEntry(data[0] * 100, "Food"));
                                        colors.add(0xFF90CAF9);
                                    }
                                    if (data[1] > 1e-6) {
                                        entries.add(new PieEntry(data[1] * 100, "Transportation"));
                                        colors.add(0xFFA5D6A7);
                                    }
                                    if (data[2] > 1e-6) {
                                        entries.add(new PieEntry(data[2] * 100, "Shelter"));
                                        colors.add(0xFFF48FB1);
                                    }
                                    if (data[3] > 1e-6) {
                                        entries.add(new PieEntry(data[3] * 100, "Entertainment"));
                                        colors.add(0xFFFFCC80);
                                    }
                                    if (data[4] > 1e-6) {
                                        entries.add(new PieEntry(data[4] * 100, "Other"));
                                        colors.add(0xFF80CBC4);
                                    }
                                    PieDataSet dataSet = new PieDataSet(entries, "Expenses");

                                    dataSet.setColors(colors);
                                    dataSet.setSliceSpace(5f);

                                    PieData pieData = new PieData(dataSet);
                                    pieData.setValueFormatter(new PercentFormatter());
                                    pieData.setValueTextSize(18f);
                                    pieData.setValueTextColor(Color.BLACK);
                                    PieChart chart = (PieChart) findViewById(R.id.chart);
                                    chart.setData(pieData);
                                    chart.setEntryLabelTextSize(18f);
                                    chart.setEntryLabelColor(Color.BLACK);
                                    chart.invalidate();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
