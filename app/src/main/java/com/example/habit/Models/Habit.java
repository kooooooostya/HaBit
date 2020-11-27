package com.example.habit.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
Представляет привычку с именем, интервалом, описанием к каждому дню
Является базовой моделью
 */
public class Habit implements Parcelable, Cloneable {

    public static final String FORMAT_DMY_PATTERN = "yyyy-MM-dd";

    private String mName;
    private final String mDescription;
    private final int mAmountOfDays;
    private int mCurrentDay;
    private final ArrayList<Description> mDescriptionArrayList;
    private final Calendar mLastComplete;       //stores the date of the last change of the habit
    private boolean mComplete;

    public Habit(String name, String description, int amountOfDays, int currentDay,
                 boolean complete, ArrayList<Description> descriptionArrayList, Calendar lastComplete) {
        mName = name;
        mDescription = description;
        mAmountOfDays = amountOfDays;
        if(descriptionArrayList.size() < amountOfDays){
            for(int i = descriptionArrayList.size(); i <= amountOfDays; i++){
                descriptionArrayList.add(new Description());
            }
        }
        mDescriptionArrayList = descriptionArrayList;
        mCurrentDay = currentDay;
        mComplete = complete;
        mLastComplete = lastComplete;
    }

    public Habit(String name, String description, int amountOfDays, int currentDay,
                 boolean complete, String descListDividedByAt, Calendar lastComplete) {
        mName = name;
        mDescription = description;
        mAmountOfDays = amountOfDays;
        mDescriptionArrayList = stringToDescriptionArrayList(descListDividedByAt);
        mCurrentDay = currentDay;
        mComplete = complete;
        mLastComplete = lastComplete;
    }
    
    public Habit(String name, String description, int amountOfDays) {
        mName = name;
        mDescription = description;
        mAmountOfDays = amountOfDays;
        mDescriptionArrayList = new ArrayList<>();
        //Populates ArrayList with empty Descriptions
        for(int i = 0; i < mAmountOfDays; i++){
            mDescriptionArrayList.add(new Description(""));
        }
        mCurrentDay = 0;
        mComplete = false;
        mLastComplete = Calendar.getInstance();
        mLastComplete.setTime(new Date());
    }



    protected Habit(Parcel in){
        mName = in.readString();
        mDescription = in.readString();
        mAmountOfDays = in.readInt();
        mCurrentDay = in.readInt();
        mDescriptionArrayList = stringToDescriptionArrayList(in.readString());

        mComplete = in.readByte() == 1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DMY_PATTERN, Locale.ENGLISH);
        mLastComplete = Calendar.getInstance();
        try {
            mLastComplete.setTime(simpleDateFormat.parse(in.readString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public int getAmountOfDays() {
        return mAmountOfDays;
    }

    public Calendar getLastComplete() {
        return mLastComplete;
    }

    public String getLastCompleteString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DMY_PATTERN, Locale.ENGLISH);
        return simpleDateFormat.format(mLastComplete.getTime());
    }


    //Возвращает описание на определенный день если оно есть, если нет возвращает пустой Description
    public Description getDescriptionForDay(int numOfDay){
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
        mDescriptionArrayList.add(numOfDay, new Description(description));
    }

    // increments mCurrentDay and check value of CurrentDay if its more than AmountOfDay
    // set complete = true
    public void incrementCurrentDay(){
        mCurrentDay++;
        mLastComplete.setTime(new Date());
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

        dest.writeString(arrayListDescriptionToString());
        dest.writeByte((byte) (mComplete ? 1 : 0));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DMY_PATTERN, Locale.ENGLISH);
        dest.writeString(simpleDateFormat.format(mLastComplete.getTime()));
    }

    //returns string which contains all descriptions separated by "@"
    public String arrayListDescriptionToString(){
        StringBuilder stringBuilder = new StringBuilder("");
        for(Description description : mDescriptionArrayList){
            stringBuilder.append(description.toString());
            stringBuilder.append("@");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    //returns ArrayList with Descriptions from string, descriptions in string must be divided by "@"
    private ArrayList<Description> stringToDescriptionArrayList(String stringDescriptionList){
        ArrayList<Description> ans = new ArrayList<>();
        for (String s : Arrays.asList(stringDescriptionList.split("@",
                stringDescriptionList.length() + 1))){
            ans.add(new Description(s));
        }
        return ans;
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

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        return new Habit(this.mName, this.mDescription, this.mAmountOfDays, this.mCurrentDay,
                this.mComplete, (ArrayList<Description>) this.mDescriptionArrayList.clone(),
                mLastComplete);
    }
}