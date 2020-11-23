package com.example.habit.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HabitSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Habit_db";
    private static int DB_VERSION = 4;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "desk";
    private static final String COLUMN_NUM_AMOUNT_OF_DAY = "amount_num";
    private static final String COLUMN_CURRENT_DAY = "current_num";
    private static final String COLUMN_IS_COMPLETE = "is_complete";


    private static final int COLUMN_NAME_INDEX = 1;
    private static final int COLUMN_DESCRIPTION_INDEX = 2;
    private static final int COLUMN_NUM_AMOUNT_OF_DAY_INDEX = 3;
    private static final int COLUMN_CURRENT_DAY_INDEX = 4;
    private static final int COLUMN_IS_COMPLETE_INDEX = 5;


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
                + COLUMN_IS_COMPLETE + " INTEGER)");
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
                contentValues.put(COLUMN_IS_COMPLETE, habit.isComplete()?"1":"0");
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
//        ChangeHabitAsync task = new ChangeHabitAsync(getWritableDatabase());
//        task.doInBackground(newHabit, oldHabit);

        Completable completable = Completable.fromAction(new Action() {
            @Override
            public void run(){
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME, newHabit.getName());
                contentValues.put(COLUMN_DESCRIPTION, newHabit.getDescription());
                contentValues.put(COLUMN_NUM_AMOUNT_OF_DAY, newHabit.getAmountOfDays());
                contentValues.put(COLUMN_CURRENT_DAY, newHabit.getCurrentDay());
                contentValues.put(COLUMN_IS_COMPLETE, newHabit.isComplete()?"1":"0");

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
                            COLUMN_NUM_AMOUNT_OF_DAY, COLUMN_CURRENT_DAY, COLUMN_IS_COMPLETE},
                    COLUMN_IS_COMPLETE + " = ?", new String[]{"0"},
                    null, null, COLUMN_ID + " DESC");
            if(cursor.moveToFirst()){
                do {
                    DescriptionSQLiteOpenHelper descriptionSQLiteOpenHelper =
                            new DescriptionSQLiteOpenHelper(mContext);
                    Habit habit = new Habit(cursor.getString(COLUMN_NAME_INDEX),
                            cursor.getString(COLUMN_DESCRIPTION_INDEX),
                            cursor.getInt(COLUMN_NUM_AMOUNT_OF_DAY_INDEX),
                            cursor.getInt(COLUMN_CURRENT_DAY_INDEX),
                            cursor.getInt(COLUMN_IS_COMPLETE_INDEX) == 1,
                            descriptionSQLiteOpenHelper.getFulledList(
                                    cursor.getString(COLUMN_NAME_INDEX))
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
}

