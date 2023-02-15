package com.example.myapplication.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.myapplication.HabitModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("testtt");
        createHabitsTables(db);
    }

    public void createHabitsTables(SQLiteDatabase db){
        List<String> HABITS_LIST = this.getEveryone();
        for(String habit_name : HABITS_LIST)
        {
            String createHabitTable = "CREATE TABLE " + habit_name + " (id  INTEGER, isDone INTEGER DEFAULT 0)";
            db.execSQL(createHabitTable);
            initialHabits(habit_name);
        }
    }

    public void initialHabits(String habitName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        int intdate = Integer.parseInt(formatter.format(date));
        cv.put("id", intdate);
        db.insert(habitName, null, cv);

    }


    public boolean addOne(String habitName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HABIT_NAME, habitName);

        long insert = db.insert(HABITS_NAMES_TABLE,null,cv);

        if(insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<String> getEveryone(){
        List<String> returnList = new ArrayList<>();

        String queryString = "SELECT HABIT_NAME FROM "+HABITS_NAMES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);

        if(cursor.moveToFirst()){

            do{
                String habitName = cursor.getString(0);
                returnList.add(habitName);
            }while(cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();
        return returnList;
    }
}
