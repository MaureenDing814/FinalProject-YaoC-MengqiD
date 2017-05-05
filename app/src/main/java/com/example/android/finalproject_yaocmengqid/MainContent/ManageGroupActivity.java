package com.example.android.finalproject_yaocmengqid.MainContent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.finalproject_yaocmengqid.Expense;
import com.example.android.finalproject_yaocmengqid.MainContent.SummaryActivity;
import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.SideMenu.GroupActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManageGroupActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FirebaseDatabase database;
    DatabaseReference peopleRef;

    ArrayList<People> peoples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);

        database = FirebaseDatabase.getInstance();
        peopleRef = FirebaseDatabase.getInstance().getReference("people");
        FirebaseRecyclerAdapter<People, ExpenseViewHolder> mAdapter = new FirebaseRecyclerAdapter<People, ExpenseViewHolder>(
                People.class,
                R.layout.group_item,
                ExpenseViewHolder.class,
                peopleRef
        ) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder holder, Expense model, int position) {
                holder.m.setText(model.getExpenseType());
                holder.mDate.setText(model.getDate());
                holder.mMoney.setText(model.getDate());
                ArrayList<People> payers = model.getPayers();
                String payerString = "";
                for (People people : payers) {
                    payerString += people.getName() + " ";
                }
                holder.mReceiver.setText("Paid by + " + payerString);

                holder.mIcon.setImageResource(R.drawable.common_full_open_on_phone);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

});
    }

}
