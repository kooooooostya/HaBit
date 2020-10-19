package com.example.habit.Models;

//The class is used to store the description of the day
public class Description {

    private String mDescription;
    private String mNameOfHabit;
    private int mDayNumber;

    public Description(String description, String nameOfHabit, int dayNumber) {
        mDescription = description;
        mNameOfHabit = nameOfHabit;
        mDayNumber = dayNumber;
    }

    public Description(String nameOfHabit, int dayNumber) {
        mNameOfHabit = nameOfHabit;
        mDayNumber = dayNumber;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getNameOfHabit() {
        return mNameOfHabit;
    }

    public int getDayNumber() {
        return mDayNumber;
    }
}
