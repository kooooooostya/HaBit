package com.example.habit.ui.listOfHabits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habit.Models.Habit;
import com.example.habit.R;
import com.example.habit.RecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListOfHabitsFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;
    FloatingActionButton mActionButtonAdd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_of_habits, container, false);

        mRecyclerView = root.findViewById(R.id.list_recycler);
        mActionButtonAdd = root.findViewById(R.id.list_floating_button);

        ArrayList<Habit> arrayList = new ArrayList<>();
        arrayList.add(new Habit("kykat", 13));
        arrayList.add(new Habit("pukat", 7));
        arrayList.add(new Habit("spat", 21));

        mRecyclerAdapter = new RecyclerAdapter(arrayList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        return root;
    }
}