package com.example.android.finalproject_yaocmengqid.MainContent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;

import java.util.ArrayList;

public class ManageGroupAdapter extends RecyclerView.Adapter<ManageGroupAdapter.ViewHolder> {
        public ArrayList<People> mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView memberName;
            public TextView memberEmail;

            public ViewHolder(TextView v) {
                super(v);
                memberName = (TextView) v.findViewById(R.id.member_name);
                memberEmail = (TextView) v.findViewById(R.id.member_email);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ManageGroupAdapter(ArrayList<People> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ManageGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            // create a new view
            TextView v = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.group_item, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.memberName.setText(mDataset.get(position).getName());
            holder.memberEmail.setText(mDataset.get(position).getEmail());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
        }



