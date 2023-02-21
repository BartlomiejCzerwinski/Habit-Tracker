package com.example.myapplication.myapplication;

import android.annotation.SuppressLint;
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

    }

    public void addHabitDailyStatus (String habitName, boolean status) {
        if(!isDataForDayExists(this.getReadableDatabase(), habitName, Integer.toString(getIdFromDate()))) {
            SQLiteDatabase db = this.getWritableDatabase();
            String addHabitLine = "INSERT INTO " + habitName + " (id, isDone) VALUES (" + getIdFromDate() + ", "+ status + ");";
            db.execSQL(addHabitLine);
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            String addHabitLine = "UPDATE " + habitName + " SET isDone = " + status + " WHERE id = " + getIdFromDate() + ";" ;
            db.execSQL(addHabitLine);
        }
        return;
    }

    @SuppressLint("Range")
    public boolean getHabitDailyStatus (String habitName) {
        if(isDataForDayExists(this.getReadableDatabase(), habitName, Integer.toString(getIdFromDate()))) {
            boolean result = false;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT isDone FROM " + habitName + " WHERE id = ?", new String[]{String.valueOf(getIdFromDate())});
            if (cursor.moveToFirst()) {
                int tmp = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isDone")));
                result = (tmp != 0);
            }
            cursor.close();
            db.close();
            return result;
        }
        return false;
    }

    public int getIdFromDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        return Integer.parseInt(formatter.format(date));
    }


    public void createHabitsTables(){
        List<String> HABITS_LIST = this.getHabitsNamesListFromHabitsNamesTable();
        for(String habit_name : HABITS_LIST)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            if(!doesTableExist(db, habit_name)) {
                String createHabitTable = "CREATE TABLE " + habit_name + " (id  INTEGER, isDone INTEGER DEFAULT 0)";
                db.execSQL(createHabitTable);
            }
        }
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[] {tableName});
        boolean exists = (cursor != null) && (cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public boolean isDataForDayExists(SQLiteDatabase db, String habitName, String id) {
        Cursor cursor = db.rawQuery("SELECT * FROM "+ habitName +" WHERE id=? ", new String[] {id});
        boolean exists = (cursor != null) && (cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public boolean addHabitToHabitsNamesTable(String habitName){
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

    public List<String> getHabitsNamesListFromHabitsNamesTable(){
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
