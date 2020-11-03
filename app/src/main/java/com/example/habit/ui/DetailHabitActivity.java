package com.example.habit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.habit.CustomView.ProgressTable;
import com.example.habit.Models.Habit;
import com.example.habit.R;
import com.example.habit.ui.listOfHabits.ListOfHabitsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailHabitActivity extends AppCompatActivity {

    public static final String EXTRA_PARCEL_DATA = "extra";
    private TextView mTextViewNum;
    private TextView mTextViewName;
    private EditText mEditTextDesc;
    private ProgressTable mProgressTable;
    private FloatingActionButton mActionButton;
    private Habit mHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        mTextViewName = findViewById(R.id.detail_name);
        mTextViewNum = findViewById(R.id.detail_num);
        mEditTextDesc = findViewById(R.id.detail_description);
        mProgressTable = findViewById(R.id.detail_progressTable);
        mActionButton = findViewById(R.id.detail_floating_button);

        mHabit = getIntent().getParcelableExtra(EXTRA_PARCEL_DATA);

        mTextViewNum.setText(String.valueOf(mHabit.getCurrentDay()));
        mTextViewName.setText(mHabit.getName());
        mEditTextDesc.setText(mHabit.getDescription(mHabit.getCurrentDay()).toString());
        mProgressTable.setPointsNum(mHabit.getAmountOfDays());
        mProgressTable.setActivePointsNum(mHabit.getCurrentDay());

        //updates the habit value and updates it in the database
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHabit.setDescription(mHabit.getCurrentDay(), mEditTextDesc.getText().toString());
                mHabit.incrementCurrentDay();
                //TODO save changes to db
                onBackPressed();
            }
        });

    }
}