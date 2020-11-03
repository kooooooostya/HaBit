package com.example.habit.Models;

import android.os.Parcel;
import android.os.Parcelable;

//The class is used to store the description for a specific day,
// contains the KKK field for storing information, the rest is needed to save
// it to the database
public class Description implements Parcelable {

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
        mDescription = "";
        mDayNumber = dayNumber;
    }

    protected Description(Parcel in) {
        mDescription = in.readString();
        mNameOfHabit = in.readString();
        mDayNumber = in.readInt();
    }

    public static final Creator<Description> CREATOR = new Creator<Description>() {
        @Override
        public Description createFromParcel(Parcel in) {
            return new Description(in);
        }

        @Override
        public Description[] newArray(int size) {
            return new Description[size];
        }
    };

    public String getDescription() {
        return mDescription;
    }

    public String getNameOfHabit() {
        return mNameOfHabit;
    }

    public int getDayNumber() {
        return mDayNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDescription);
        dest.writeString(mNameOfHabit);
        dest.writeInt(mDayNumber);
    }

    @Override
    public String toString() {
        return mDescription;
    }
}
