package com.example.habit.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DescriptionSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Description_db";
    private static int DB_VERSION = 1;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME_OF_HABIT = "name";
    private static final String COLUMN_DESCRIPTION = "desc";
    private static final String COLUMN_NUM_OF_DAY = "num";

    private Context mContext;

    DescriptionSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ DB_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME_OF_HABIT + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_NUM_OF_DAY + " INTEGER)");
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

    void insertToDBAsync(Description description){
        AddDescriptionAsync task = new AddDescriptionAsync(getWritableDatabase());
        task.doInBackground(description);
    }

//    void changeAsync(Description newDescription, Description oldDescription){
//         task = new ChangeTimeBusinessAsync(getWritableDatabase());
//        task.doInBackground(newDescription, oldDescription);
//    }


    //Получает на вход имя задачи для которой необходимо описание,
    // на выход дает список описаний отсортированый(певый день под индексом 0 и т.д.)
    ArrayList<Description> getFulledList(String nameOfHabit){

        ArrayList<Description> descriptionArrayList = new ArrayList<>();

        try {
            Cursor cursor = this.getReadableDatabase().query(DB_NAME,
                    new String[]{COLUMN_ID, COLUMN_NAME_OF_HABIT, COLUMN_DESCRIPTION, COLUMN_NUM_OF_DAY},
                    COLUMN_NAME_OF_HABIT + " = ?", new String[]{nameOfHabit},
                    null, null, COLUMN_NUM_OF_DAY + " ASK");
            if(cursor.moveToFirst()){
                do {
                        Description description = new Description(cursor.getString(1),
                                cursor.getString(2), cursor.getInt(3));
                        descriptionArrayList.add(description);

                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (SQLException e) {
            Toast.makeText(mContext, "db error", Toast.LENGTH_SHORT).show();
        }
        return descriptionArrayList;
    }

    //Вставляет description в базу данных
    private static class AddDescriptionAsync extends AsyncTask<Description, Void, Boolean> {

        SQLiteDatabase mDatabase;

        AddDescriptionAsync(SQLiteDatabase db) {
            mDatabase = db;
        }
        @Override
        protected Boolean doInBackground(Description... descriptions) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME_OF_HABIT, descriptions[0].getNameOfHabit());
                contentValues.put(COLUMN_DESCRIPTION, descriptions[0].getDescription());
                contentValues.put(COLUMN_NUM_OF_DAY, descriptions[0].getDayNumber());
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

