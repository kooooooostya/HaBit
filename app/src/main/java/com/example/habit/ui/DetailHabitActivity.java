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


    private boolean isSecondClick = false;

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

        //on the first press it updates the mHabit value and the icon on the button,
        //on the second press it calls finish
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSecondClick){
                    finish();
                }else {
                    mHabit.setDescription(mHabit.getCurrentDay(), mEditTextDesc.getText().toString());
                    mHabit.incrementCurrentDay();
                    Intent resultData = new Intent();
                    resultData.putExtra(ListOfHabitsFragment.INTENT_EXTRA_DATA, mHabit);
                    mProgressTable.completeOne();
                    mTextViewNum.setText(String.valueOf(mHabit.getCurrentDay()));
                    mActionButton.setImageResource(R.drawable.ic_baseline_arrow_back_24);
                    isSecondClick = true;
                }

            }
        });

    }
}