package com.example.myapplication.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.myapplication.databinding.FragmentStatisticsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticsManager {
    CalendarView calendarView;
    private Context context;
    private FragmentStatisticsBinding binding;

    public StatisticsManager(CalendarView calendarView, Context context, FragmentStatisticsBinding binding) {
        this.calendarView = calendarView;
        this.context = context;
        this.binding = binding;

        long dateInMillis = 1667251200000L;  // February 29, 2022
        int color = Color.RED;

        setCalendarCellColorForDate(calendarView, dateInMillis, color);
        setHabitsSpinner();
    }

    public static void setCalendarCellColorForDate(CalendarView calendarView, long dateInMillis, int color) {
        calendarView.setDate(dateInMillis);
        ViewGroup dayPickerView = (ViewGroup) calendarView.getChildAt(0);
        ViewTreeObserver vto = dayPickerView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View dayView = dayPickerView.getChildAt((int) (calendarView.getDate() - 1));
                if (dayView instanceof TextView) {
                    TextView dayTextView = (TextView) dayView;
                    dayTextView.setTextColor(color);
                }
            }
        });
    }


    public void setHabitsSpinner() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<String> habitsNamesList = dataBaseHelper.getHabitsNamesListFromHabitsNamesTable();
        habitsNamesList.add("All Habits");

        Spinner spinner = (Spinner) binding.getRoot().findViewById(R.id.habitsSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, habitsNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(habitsNamesList.size()-1);
        System.out.println("setHabitsSpinner Selected item: " + spinner.getSelectedItem().toString());
        loadStatisicsDataOnSpinnerSelect();
    }

    public void loadStatisicsDataOnSpinnerSelect() {
        Spinner spinner = (Spinner) binding.getRoot().findViewById(R.id.habitsSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (spinner.getSelectedItem().toString() == "All Habits") {
                   // actions for one habit
               }
               else {// tutaj skonczylem
                    String habitName = spinner.getSelectedItem().toString();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                    String sd = dataBaseHelper.getHabitStartDate(habitName);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                   try {
                       Date startDate = sdf.parse(sd);
                       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                           String localDate = LocalDate.now().toString();
                           Date now = sdf.parse(localDate);
                           long diffInDays = (now.getTime() - startDate.getTime())/ (24 * 60 * 60 * 1000);
                           diffInDays += 1;
                           System.out.println("minelo:"+ diffInDays);
                           dataBaseHelper.countDoneDays(habitName);
                       }
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }

               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
