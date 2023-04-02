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
        Date date = new Date();
        String createTableStatement = "CREATE TABLE " + HABITS_NAMES_TABLE + " ("+ HABIT_NAME +" TEXT UNIQUE, Start_date DATE)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addHabitDailyStatus (String habitName, boolean status) {
        habitName = convertHabitNameIntoDb(habitName);
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
        habitName = convertHabitNameIntoDb(habitName);
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

    @SuppressLint("Range")
    public String getHabitStartDate (String habitName) {
        habitName = convertHabitNameIntoDb(habitName);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Start_date FROM " +HABITS_NAMES_TABLE + " WHERE HABIT_NAME = ?", new String[]{habitName});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public int getIdFromDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(formatter.format(date));
    }


    public void createHabitsTables(){
        List<String> HABITS_LIST = this.getHabitsNamesListFromHabitsNamesTable();
        for(String habit_name : HABITS_LIST)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            if(!doesTableExist(db, habit_name)) {
                habit_name = convertHabitNameIntoDb(habit_name);
                String createHabitTable = "CREATE TABLE " + habit_name + " (id  INTEGER, isDone INTEGER DEFAULT 0)";
                db.execSQL(createHabitTable);
            }
        }
    }

    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        tableName = convertHabitNameIntoDb(tableName);
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[] {tableName});
        boolean exists = (cursor != null) && (cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public int countDoneDays(String habitName) {
        habitName = convertHabitNameIntoDb(habitName);
        SQLiteDatabase db = this.getReadableDatabase();
        if(doesTableExist(db, habitName)) {
            String query = "SELECT COUNT(*) FROM " + habitName + "WHERE isDone = 1";
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + habitName + " WHERE isDone = 1", null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public int countMonthDoneDays(String habitName, Date startDate) {
        habitName = convertHabitNameIntoDb(habitName);
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sD = sdf.format(startDate);
        if(doesTableExist(db, habitName)) {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + habitName + " WHERE isDone = 1 " + "AND id >= ?", new String[] {sD});
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public int countDoneDaysFromTimePeriod(String habitName, String startDate, String endDate) {
        habitName = convertHabitNameIntoDb(habitName);
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if(doesTableExist(db, habitName)) {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + habitName + " WHERE isDone = 1 " +
                    "AND id >= ? AND id <= ?", new String[]{endDate, startDate});
            cursor.moveToFirst();
            System.out.println("POLICZONO: " + cursor.getInt(0));
            return cursor.getInt(0);
        }
        return 0;
    }

    public int countWeekDoneDays(String habitName, Date startDate) {
        habitName = convertHabitNameIntoDb(habitName);
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String sD = sdf.format(startDate);
        if(doesTableExist(db, habitName)) {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + habitName + " WHERE isDone = 1 " + "AND id >= ?", new String[] {sD});
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return 0;
    }

    public boolean isDataForDayExists(SQLiteDatabase db, String habitName, String id) {
        habitName = convertHabitNameIntoDb(habitName);
        Cursor cursor = db.rawQuery("SELECT * FROM "+ habitName +" WHERE id=? ", new String[] {id});
        boolean exists = (cursor != null) && (cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public String getTodayDateForDatabase() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatedDate = sdf.format(date);
        return formatedDate;
    }

    public boolean addHabitToHabitsNamesTable(String habitName){
        habitName = convertHabitNameIntoDb(habitName);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HABIT_NAME, habitName);
        cv.put("Start_date", getTodayDateForDatabase());

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
        for (int i = 0; i < returnList.size(); i++) {
            returnList.set(i, convertHabitNameFromDb(returnList.get(i)));
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public void deleteHabitFromDb(String habitName) {
        habitName = convertHabitNameIntoDb(habitName);
        SQLiteDatabase db = this.getWritableDatabase();
        String dropTableQuery = "DROP TABLE " + habitName;
        db.execSQL(dropTableQuery);
        String deleteHabitFromQuery = "DELETE FROM HABITS_NAMES_TABLE WHERE HABIT_NAME = \"" + habitName + "\"";
        db.execSQL(deleteHabitFromQuery);
        //Cursor cursor = db.rawQuery("DELETE FROM HABITS_NAMES_TABLE WHERE HABIT_NAME = ?;", new String[] {habitName});
    }

    public String convertHabitNameIntoDb(String habitName) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < habitName.length(); i++) {
            if (habitName.charAt(i) == ' ') {
                sb.append('_');
            } else {
                sb.append(habitName.charAt(i));
            }
        }
        return sb.toString();
    }

    public String convertHabitNameFromDb(String habitName) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < habitName.length(); i++) {
            if (habitName.charAt(i) == '_') {
                sb.append(' ');
            } else {
                sb.append(habitName.charAt(i));
            }
        }
        return sb.toString();
    }

}
