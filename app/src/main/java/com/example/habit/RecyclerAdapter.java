package com.example.habit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit.CustomView.ProgressTable;
import com.example.habit.Models.Habit;
import com.example.habit.ui.DetailHabitActivity;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.HabitViewHolder> {

    ArrayList<Habit> mHabitArrayList;

    public RecyclerAdapter(ArrayList<Habit> habitArrayList) {
        mHabitArrayList = habitArrayList;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HabitViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HabitViewHolder holder, final int position) {
        Habit currentHabit = mHabitArrayList.get(position);
        holder.mNameTextView.setText(currentHabit.getName());
        holder.mCountTextView.setText(String.valueOf(currentHabit.getCurrentDay()));
        holder.mProgressTable.setPointsNum(currentHabit.getAmountOfDays());
        holder.mProgressTable.setActivePointsNum(currentHabit.getCurrentDay());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailHabitActivity.class);
                intent.putExtra(DetailHabitActivity.EXTRA_PARCEL_DATA, mHabitArrayList.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHabitArrayList.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder{

        public CardView mCardView;
        public TextView mNameTextView;
        public TextView mCountTextView;
        public ProgressTable mProgressTable;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.item_name_tv);
            mCountTextView = itemView.findViewById(R.id.item_count_tv);
            mProgressTable = itemView.findViewById(R.id.detail_progressTable);
            mCardView = itemView.findViewById(R.id.item_cart_view);
        }
    }
}
