package com.example.android.finalproject_yaocmengqid.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;

import java.util.ArrayList;

import static com.example.android.finalproject_yaocmengqid.R.id.check;
import static com.example.android.finalproject_yaocmengqid.R.id.choose_name;

/**
 * Created by YaoChen on 5/7/17.
 */

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.PeopleViewHolder>{
    private ArrayList<People> mDataset;
    private ArrayList<Boolean> isChecked;

    public ChooseAdapter(ArrayList<People> mDataset, ArrayList isChecked) {
        this.mDataset = mDataset;
        this.isChecked = isChecked;
    }

    public static class PeopleViewHolder extends RecyclerView.ViewHolder{
        public TextView mName;
        public CheckBox mCheck;

        public PeopleViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(choose_name);
            mCheck = (CheckBox)itemView.findViewById(check);

        }
    }

    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_item, parent, false);
        PeopleViewHolder vh = new PeopleViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, final int position) {
        holder.mName.setText(mDataset.get(position).getName());
        holder.mCheck.setChecked(isChecked.get(position));
        holder.mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                isChecked.set(position, checked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
