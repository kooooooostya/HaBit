package com.example.habit.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HabitSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Habit_db";
    private static int DB_VERSION = 8;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "desk";
    private static final String COLUMN_NUM_AMOUNT_OF_DAY = "amount_num";
    private static final String COLUMN_CURRENT_DAY = "current_num";
    private static final String COLUMN_IS_COMPLETE = "is_complete";
    private static final String COLUMN_DESCRIPTION_LIST = "description_list";
    private static final String COLUMN_LAST_COMPLETE = "last_complete";


    private static final int COLUMN_NAME_INDEX = 1;
    private static final int COLUMN_DESCRIPTION_INDEX = 2;
    private static final int COLUMN_NUM_AMOUNT_OF_DAY_INDEX = 3;
    private static final int COLUMN_CURRENT_DAY_INDEX = 4;
    private static final int COLUMN_IS_COMPLETE_INDEX = 5;
    private static final int COLUMN_DESCRIPTION_LIST_INDEX = 6;
    private static final int COLUMN_LAST_COMPLETE_INDEX = 7;


    private final Context mContext;

    public HabitSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ DB_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_NUM_AMOUNT_OF_DAY + " INTEGER, " + COLUMN_CURRENT_DAY + " INTEGER, "
                + COLUMN_IS_COMPLETE + " INTEGER, "+ COLUMN_DESCRIPTION_LIST + " TEXT, "
                + COLUMN_LAST_COMPLETE + " TEXT)");
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

    //inserts habit async to db
    public void insertToDBAsync(final Habit habit){
        Completable completable = Completable.fromAction(new Action() {
            @Override
            public void run() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME, habit.getName());
                contentValues.put(COLUMN_DESCRIPTION, habit.getDescription());
                contentValues.put(COLUMN_NUM_AMOUNT_OF_DAY, habit.getAmountOfDays());
                contentValues.put(COLUMN_CURRENT_DAY, habit.getCurrentDay());
                contentValues.put(COLUMN_DESCRIPTION_LIST, habit.arrayListDescriptionToString());
                contentValues.put(COLUMN_IS_COMPLETE, habit.isComplete()?"1":"0");
                contentValues.put(COLUMN_LAST_COMPLETE, habit.getLastCompleteString());
                getWritableDatabase().insert(DB_NAME, null, contentValues);
            }
        }).subscribeOn(Schedulers.io());

        DisposableCompletableObserver observer = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(mContext, "db error", Toast.LENGTH_SHORT).show();
            }
        };

        completable.subscribe(observer);

    }

    public void changeAsync(final Habit newHabit, final Habit oldHabit){
        Completable completable = Completable.fromAction(new Action() {
            @Override
            public void run(){
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME, newHabit.getName());
                contentValues.put(COLUMN_DESCRIPTION, newHabit.getDescription());
                contentValues.put(COLUMN_NUM_AMOUNT_OF_DAY, newHabit.getAmountOfDays());
                contentValues.put(COLUMN_CURRENT_DAY, newHabit.getCurrentDay());
                contentValues.put(COLUMN_DESCRIPTION_LIST, newHabit.arrayListDescriptionToString());
                contentValues.put(COLUMN_IS_COMPLETE, newHabit.isComplete()?"1":"0");
                contentValues.put(COLUMN_LAST_COMPLETE, newHabit.getLastCompleteString());

                getWritableDatabase().update(DB_NAME, contentValues,
                        COLUMN_NAME + " = ?", new String[]{oldHabit.getName()});
            }
        });

        DisposableCompletableObserver observer = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(mContext, "db error", Toast.LENGTH_SHORT).show();
            }
        };

        completable.subscribe(observer);

    }


    //returns full list of uncompleted Habits
    public ArrayList<Habit> getFulledListOfActiveHabit(){

        ArrayList<Habit> descriptionArrayList = new ArrayList<>();

        try {
            Cursor cursor = this.getReadableDatabase().query(DB_NAME,
                    new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_DESCRIPTION,
                            COLUMN_NUM_AMOUNT_OF_DAY, COLUMN_CURRENT_DAY, COLUMN_IS_COMPLETE,
                            COLUMN_DESCRIPTION_LIST, COLUMN_LAST_COMPLETE},
                    COLUMN_IS_COMPLETE + " = ?", new String[]{"0"},
                    null, null, COLUMN_ID + " DESC");

            SimpleDateFormat simpleDateFormat = new
                    SimpleDateFormat(Habit.FORMAT_DMY_PATTERN, Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();

            if(cursor.moveToFirst()){
                do {

                    calendar.setTime(Objects.requireNonNull(simpleDateFormat.
                            parse(cursor.getString(COLUMN_LAST_COMPLETE_INDEX))));

                    Habit habit = new Habit(cursor.getString(COLUMN_NAME_INDEX),
                            cursor.getString(COLUMN_DESCRIPTION_INDEX),
                            cursor.getInt(COLUMN_NUM_AMOUNT_OF_DAY_INDEX),
                            cursor.getInt(COLUMN_CURRENT_DAY_INDEX),
                            cursor.getInt(COLUMN_IS_COMPLETE_INDEX) == 1,
                            cursor.getString(COLUMN_DESCRIPTION_LIST_INDEX), calendar
                    );
                    descriptionArrayList.add(habit);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (SQLException | ParseException e) {
            Toast.makeText(mContext, "db error", Toast.LENGTH_SHORT).show();
        }
        return descriptionArrayList;
    }
}

