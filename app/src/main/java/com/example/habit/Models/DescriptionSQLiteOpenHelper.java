package com.example.habit.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DescriptionSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Description_db";
    private static int DB_VERSION = 1;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME_OF_HABIT = "name";
    private static final String COLUMN_DESCRIPTION = "desc";
    private static final String COLUMN_NUM_OF_DAY = "num";

    //private final Context mContext;

    DescriptionSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //mContext = context;
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

    //inserts Description to db
    void insertToDBAsync(final Description description){
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_NAME_OF_HABIT, description.getNameOfHabit());
                contentValues.put(COLUMN_DESCRIPTION, description.getDescription());
                contentValues.put(COLUMN_NUM_OF_DAY, description.getDayNumber());
                getWritableDatabase().insert(DB_NAME, null, contentValues);
            }
        }).subscribeOn(Schedulers.io()).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }
        });


    }

    //Получает на вход имя задачи для которой необходимо описание,
    // на выход дает список описаний отсортированый(певый день под индексом 0 и т.д.)
    ArrayList<Description> getFulledList(String nameOfHabit){

        ArrayList<Description> descriptionArrayList = new ArrayList<>();

        try {
            Cursor cursor = this.getReadableDatabase().query(DB_NAME,
                    new String[]{COLUMN_ID, COLUMN_NAME_OF_HABIT, COLUMN_DESCRIPTION, COLUMN_NUM_OF_DAY},
                    COLUMN_NAME_OF_HABIT + " = ?", new String[]{nameOfHabit},
                    null, null, COLUMN_NUM_OF_DAY + " DESC");
            if(cursor.moveToFirst()){
                do {
                        Description description = new Description(cursor.getString(1),
                                cursor.getString(2), cursor.getInt(3));
                        descriptionArrayList.add(description);

                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (SQLException e) {
            //Toast.makeText(mContext, "db error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return descriptionArrayList;
    }
}

