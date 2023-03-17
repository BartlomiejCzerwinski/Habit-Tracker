package com.example.myapplication.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.myapplication.databinding.FragmentStatisticsBinding;

import java.util.ArrayList;
import java.util.Calendar;
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

        Spinner spinner = (Spinner) binding.getRoot().findViewById(R.id.habitsSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, habitsNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
