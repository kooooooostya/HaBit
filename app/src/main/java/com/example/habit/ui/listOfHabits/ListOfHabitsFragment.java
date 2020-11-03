package com.example.habit.ui.listOfHabits;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit.Models.Habit;
import com.example.habit.R;
import com.example.habit.RecyclerAdapter;
import com.example.habit.ui.AddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListOfHabitsFragment extends Fragment {

    public static final int INTENT_REQUEST_CODE = 1;
    //Result code in intent means everything is fine
    public static final int RESULT_CODE_OK = 152;

    public static final String INTENT_EXTRA_DATA = "data";

    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;
    FloatingActionButton mActionButtonAdd;

    ArrayList<Habit> mHabitArrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_of_habits, container, false);

        mRecyclerView = root.findViewById(R.id.list_recycler);
        mActionButtonAdd = root.findViewById(R.id.list_floating_button);


        mHabitArrayList.add(new Habit("kykat", "", 13));
        mHabitArrayList.add(new Habit("pukat","",7));
        mHabitArrayList.add(new Habit("spat", "",21));

        mRecyclerAdapter = new RecyclerAdapter(mHabitArrayList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_CODE);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_REQUEST_CODE){
            assert data != null;
            Habit habit = data.getParcelableExtra(INTENT_EXTRA_DATA);
            mHabitArrayList.add(habit);
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }
}