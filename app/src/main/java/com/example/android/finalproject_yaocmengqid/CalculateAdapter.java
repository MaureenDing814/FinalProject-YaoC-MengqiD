package com.example.android.finalproject_yaocmengqid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Maureen_Ding on 4/24/17.
 */


    public class CalculateAdapter extends RecyclerView.Adapter<CalculateAdapter.ViewHolder> {
        private ArrayList<Plan> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mAction;
            public TextView mMoney;
            public TextView mReceiver;

            public ViewHolder(TextView v) {
                super(v);
                mAction = (TextView)v.findViewById(R.id.action);
                mMoney = (TextView)v.findViewById(R.id.money);
                mReceiver = (TextView)v.findViewById(R.id.receiver_name);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public CalculateAdapter(ArrayList<Plan> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public CalculateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.calculate_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mAction.setText(mDataset.get(position).getAction());
            holder.mMoney.setText(mDataset.get(position).getMoney());
            holder.mReceiver.setText(mDataset.get(position).getReceiver());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

