package com.example.habit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.habit.Models.Habit;
import com.example.habit.R;
import com.example.habit.ui.listOfHabits.ListOfHabitsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextDesc;
    private EditText mEditTextNum;
    private FloatingActionButton mActionButton;

    private Habit mHabit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mEditTextName = findViewById(R.id.add_name_et);
        mEditTextDesc = findViewById(R.id.add_description_et);
        mEditTextNum = findViewById(R.id.add_num_day_et);
        mActionButton = findViewById(R.id.add_action_button);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditTextName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "You must give the name of the habit",
                            Toast.LENGTH_LONG).show();
                }else if(mEditTextNum.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "You must set the number of days",
                            Toast.LENGTH_LONG).show();
                }else {
                    mHabit = new Habit(
                            mEditTextName.getText().toString(),
                            mEditTextDesc.getText().toString(),
                            Integer.parseInt(mEditTextNum.getText().toString())
                    );
                    Intent intent = new Intent();
                    intent.putExtra(ListOfHabitsFragment.INTENT_EXTRA_DATA, mHabit);
                    setResult(ListOfHabitsFragment.INTENT_REQUEST_CODE, intent);
                    finish();
                }
            }
        });
    }
}