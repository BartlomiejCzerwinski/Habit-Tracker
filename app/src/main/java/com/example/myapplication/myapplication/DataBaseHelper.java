package com.example.myapplication.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.myapplication.HabitModel;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String HABITS_NAMES_TABLE = "HABITS_NAMES_TABLE";
    public static final String HABIT_NAME = "HABIT_NAME";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "habits.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + HABITS_NAMES_TABLE + " ("+ HABIT_NAME +" TEXT UNIQUE)";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(HabitModel habitModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HABIT_NAME, habitModel.getName());

        long insert = db.insert(HABITS_NAMES_TABLE,null,cv);

        if(insert == -1){
            return false;
        }
        else{
            return true;
        }
    }
}
