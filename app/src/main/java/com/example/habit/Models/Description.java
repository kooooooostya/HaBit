package com.example.habit.Models;

//The class is used to store the description for a day
public class Description{

    private final String mDescription;

    public Description(String description) {
        mDescription = description;
    }

    public Description() {
        mDescription = "";
    }


    public String getDescription() {
        return mDescription;
    }

    @Override
    public String toString() {
        return mDescription;
    }
}
