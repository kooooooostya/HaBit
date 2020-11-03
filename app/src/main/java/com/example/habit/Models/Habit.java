package com.example.habit.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/*
Представляет привычку с именем, интервалом, описанием к каждому дню
Является базовой моделью
 */
public class Habit implements Parcelable {
    private String mName;
    private String mDescription;
    private final int mAmountOfDays;
    private int mCurrentDay;
    private final ArrayList<Description> mDescriptionArrayList;
    private boolean mComplete;

    public Habit(String name, int amountOfDays, ArrayList<Description> descriptionArrayList) {
        mName = name;
        mDescription = "";
        mAmountOfDays = amountOfDays;
        mDescriptionArrayList = descriptionArrayList;
        mCurrentDay = 0;
        mComplete = false;
    }

    public Habit(String name, String description, int amountOfDays, ArrayList<Description> descriptionArrayList) {
        mName = name;
        mDescription = description;
        mAmountOfDays = amountOfDays;
        mDescriptionArrayList = descriptionArrayList;
        mCurrentDay = 0;
        mComplete = false;
    }

    public Habit(String name, String description, int amountOfDays) {
        mName = name;
        mDescription = description;
        mAmountOfDays = amountOfDays;
        mDescriptionArrayList = new ArrayList<>();
        //Populates ArrayList with empty Descriptions
        for(int i = 0; i < mAmountOfDays; i++){
            mDescriptionArrayList.add(new Description(mName, i));
        }
        mCurrentDay = 0;
        mComplete = false;
    }



    protected Habit(Parcel in) {
        mName = in.readString();
        mDescription = in.readString();
        mAmountOfDays = in.readInt();
        mCurrentDay = in.readInt();
        //TODO протестить ибо вохможно ткт мясо(информативно)
        Description[] descriptions = in.createTypedArray(Description.CREATOR);
        mDescriptionArrayList = new ArrayList<>();
        if(descriptions != null) mDescriptionArrayList.addAll(Arrays.asList(descriptions));
        mComplete = in.readByte() != 0;
    }

    public int getCurrentDay() {
        return mCurrentDay;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getAmountOfDays() {
        return mAmountOfDays;
    }

    public Description getDescription(int numOfDay){
        return mDescriptionArrayList.get(numOfDay);
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    // устанавливает описание на определенного дня
    public void setDescription(int numOfDay, Description description){
        mDescriptionArrayList.add(numOfDay, description);
    }

    // устанавливает описание на определенного дня
    public void setDescription(int numOfDay, String description){
        mDescriptionArrayList.add(numOfDay, new Description(description, mName, numOfDay));
    }

    public void setCurrentDay(int currentDay) {
        mCurrentDay = currentDay;
    }

    // increments mCurrentDay and check value of CurrentDay if its more than AmountOfDay
    // set complete = true
    public void incrementCurrentDay(){
        mCurrentDay++;
        if(mCurrentDay >= mAmountOfDays){
            mCurrentDay = mAmountOfDays;
            mComplete = true;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeInt(mAmountOfDays);
        dest.writeInt(mCurrentDay);

        Description[] descriptionsArray = new Description[mDescriptionArrayList.size()];
        for (int i = 0; i < mDescriptionArrayList.size(); i++){
            descriptionsArray[i] = mDescriptionArrayList.get(i);
        }

        dest.writeParcelableArray(descriptionsArray, 0);
        dest.writeByte((byte) (mComplete ? 1 : 0));
    }

    public static final Parcelable.Creator<Habit> CREATOR = new Parcelable.Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel source) {
            return new Habit(source);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };


}
