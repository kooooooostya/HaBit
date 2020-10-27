package com.example.habit.Models;

import java.util.ArrayList;

/*
Представляет привычку с именем, интервалом, описанием к каждому дню
Является базовой моделью
 */
public class Habit {
    private String mName;
    private int mAmountOfDays;
    private int mCurrentDay;
    private ArrayList<Description> mDescriptions;
    private boolean mComplete;

    public Habit(String name, int amountOfDays, ArrayList<Description> descriptions) {
        mName = name;
        mAmountOfDays = amountOfDays;
        mDescriptions = descriptions;
        mCurrentDay = 0;
        mComplete = false;
    }

    public Habit(String name, int amountOfDays) {
        mName = name;
        mAmountOfDays = amountOfDays;
        mDescriptions = new ArrayList<>();
        //Populates ArrayList with empty Descriptions
        for(int i = 0; i < mAmountOfDays; i++){
            mDescriptions.add(new Description(mName, i));
        }
        mCurrentDay = 0;
        mComplete = false;
    }

    public int getCurrentDay() {
        return mCurrentDay;
    }

    public String getName() {
        return mName;
    }

    public int getAmountOfDays() {
        return mAmountOfDays;
    }

    public Description getDescription(int numOfDay){
        return mDescriptions.get(numOfDay);
    }

    public boolean isComplete() {
        return mComplete;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    // устанавливает описание на определенного дня
    public void setDescription(int numOfDay, Description description){
        mDescriptions.add(numOfDay, description);
    }

    // устанавливает описание на определенного дня
    public void setDescription(int numOfDay, String description){
        mDescriptions.add(numOfDay, new Description(description, mName, numOfDay));
    }

    public void setCurrentDay(int currentDay) {
        mCurrentDay = currentDay;
    }

}
