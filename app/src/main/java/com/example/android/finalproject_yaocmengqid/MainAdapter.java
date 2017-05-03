package com.example.android.finalproject_yaocmengqid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maureen_Ding on 4/24/17.
 */

    public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
        public ArrayList<Expense> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView mIcon;
            public TextView mName;
            public TextView mDate;
            public TextView mMoney;
            public TextView mReceiver;
            public View myView;

            public ViewHolder(TextView v) {
                super(v);
                mIcon = (ImageView) v.findViewById(R.id.icon);
                mName = (TextView)v.findViewById(R.id.item_name);
                mDate = (TextView)v.findViewById(R.id.date);
                mMoney = (TextView)v.findViewById(R.id.money);
                mReceiver = (TextView)v.findViewById(R.id.receiver_name);
                myView = v;
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MainAdapter(ArrayList<Expense> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mName.setText(mDataset.get(position).getExpenseType());
            holder.mDate.setText(mDataset.get(position).getDate());
            holder.mMoney.setText(mDataset.get(position).getDate());
            ArrayList<People> payers = mDataset.get(position).getPayers();
            String payerString = "";
            for (People people : payers) {
                payerString += people.getName() + " ";
            }
            holder.mReceiver.setText("Paid by + " + payerString);


            holder.mIcon.setImageResource(R.drawable.common_full_open_on_phone);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void groupSummary(View view){

        }
    }
