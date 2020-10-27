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
    private static int DB_VERSION = 1;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUM_OF_DAY = "num";
    private static final String COLUMN_IS_COMPLETE = "is_complete";

    private Context mContext;

    HabitSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ DB_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, " + COLUMN_NUM_OF_DAY + " INTEGER)");
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

    void insertToDBAsync(Habit habit){
        AddHabitAsync task = new AddHabitAsync(getWritableDatabase());
        task.doInBackground(habit);
    }

//    void changeAsync(Description newDescription, Description oldDescription){
//         task = new ChangeTimeBusinessAsync(getWritableDatabase());
//        task.doInBackground(newDescription, oldDescription);
//    }

    ArrayList<Habit> getFulledListOfActiveHabit(){

        ArrayList<Habit> descriptionArrayList = new ArrayList<>();

        try {
            Cursor cursor = this.getReadableDatabase().query(DB_NAME,
                    new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_NUM_OF_DAY, COLUMN_IS_COMPLETE},
                    COLUMN_NAME + " = ?", new String[]{"0"},
                    null, null, COLUMN_NUM_OF_DAY + " ASK");
            if(cursor.moveToFirst()){
                do {
                    DescriptionSQLiteOpenHelper descriptionSQLiteOpenHelper = new DescriptionSQLiteOpenHelper(mContext);
                    Habit habit = new Habit(cursor.getString(1),
                            cursor.getInt(3),
                            descriptionSQLiteOpenHelper.getFulledList(cursor.getString(1))
                    );
                    descriptionArrayList.add(habit);
                    cursor.close();
                }while (cursor.moveToNext());

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
                contentValues.put(COLUMN_NUM_OF_DAY, habits[0].isComplete()?"1":"0");
                mDatabase.insert(DB_NAME, null, contentValues);
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
    }

//    private static class ChangeTimeBusinessAsync extends AsyncTask<Description, Void, Integer> {
//
//        SQLiteDatabase mDatabase;
//
//        ChangeTimeBusinessAsync(SQLiteDatabase db) {
//            mDatabase = db;
//        }
//
//        @Override
//        protected Integer doInBackground(Description... descriptions) {
//            try {
//                //businesses[0] - new, businesses[1] - old
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(COLUMN_NAME_OF_HABIT, descriptions[0].getNameOfHabit());
//                contentValues.put(COLUMN_DESCRIPTION, descriptions[0].getDescription());
//                contentValues.put(COLUMN_NUM_OF_DAY, descriptions[0].getDayNumber());
//
//                return mDatabase.update(DB_NAME, contentValues,
//                        COLUMN_DESCRIPTION + " = ?", new String[]{businesses[1].getName()});
//            } catch (SQLException e) {
//                return 0;
//            }
//        }
//    }
}

