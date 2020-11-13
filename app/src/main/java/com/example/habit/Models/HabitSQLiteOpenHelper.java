package com.example.habit.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

public class HabitSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Habit_db";
    private static int DB_VERSION = 2;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUM_OF_DAY = "num";
    private static final String COLUMN_IS_COMPLETE = "is_complete";

    private final Context mContext;

    public HabitSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ DB_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, " + COLUMN_NUM_OF_DAY + " INTEGER, " + COLUMN_IS_COMPLETE + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        DB_VERSION = newVersion;
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        DB_VERSION = newVersion;
        onCreate(db);
    }

    public void insertToDBAsync(Habit habit){
        AddHabitAsync task = new AddHabitAsync(getWritableDatabase());
        task.doInBackground(habit);
    }

    public void changeAsync(Habit newHabit, Habit oldHabit){
        ChangeHabitAsync task = new ChangeHabitAsync(getWritableDatabase());
        task.doInBackground(newHabit, oldHabit);
    }

    public ArrayList<Habit> getFulledListOfActiveHabit(){

        ArrayList<Habit> descriptionArrayList = new ArrayList<>();

        try {
            Cursor cursor = this.getReadableDatabase().query(DB_NAME,
                    new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_NUM_OF_DAY, COLUMN_IS_COMPLETE},
                    COLUMN_IS_COMPLETE + " = ?", new String[]{"0"},
                    null, null, COLUMN_ID + " DESC");
            if(cursor.moveToFirst()){
                do {
                    DescriptionSQLiteOpenHelper descriptionSQLiteOpenHelper = new DescriptionSQLiteOpenHelper(mContext);
                    Habit habit = new Habit(cursor.getString(1),
                            cursor.getInt(2),
                            descriptionSQLiteOpenHelper.getFulledList(cursor.getString(1))
                    );
                    descriptionArrayList.add(habit);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (SQLException e) {
            Toast.makeText(mContext, "db error", Toast.LENGTH_SHORT).show();
        }
        return descriptionArrayList;
    }

    //Вставляет description в базу данных
    private static class AddHabitAsync extends AsyncTask<Habit, Void, Boolean> {

        SQLiteDatabase mDatabase;

        AddHabitAsync(SQLiteDatabase db) {
            mDatabase = db;
        }
        @Override
        protected Boolean doInBackground(Habit... habits) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME, habits[0].getName());
                contentValues.put(COLUMN_NUM_OF_DAY, habits[0].getAmountOfDays());
                contentValues.put(COLUMN_IS_COMPLETE, habits[0].isComplete()?"1":"0");
                mDatabase.insert(DB_NAME, null, contentValues);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    private static class ChangeHabitAsync extends AsyncTask<Habit, Void, Integer> {

        SQLiteDatabase mDatabase;

        ChangeHabitAsync(SQLiteDatabase db) {
            mDatabase = db;
        }

        @Override
        protected Integer doInBackground(Habit... habits) {
            try {
                //businesses[0] - new, businesses[1] - old
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME, habits[0].getName());
                contentValues.put(COLUMN_NUM_OF_DAY, habits[0].getAmountOfDays());
                contentValues.put(COLUMN_IS_COMPLETE, habits[0].isComplete()?"1":"0");

                return mDatabase.update(DB_NAME, contentValues,
                        COLUMN_NAME + " = ?", new String[]{habits[1].getName()});
            } catch (SQLException e) {
                return 0;
            }
        }
    }
}

