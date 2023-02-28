package com.example.myapplication.myapplication;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsManager {
    CalendarView calendarView;

    public StatisticsManager(CalendarView calendarView) {
        this.calendarView = calendarView;

        long dateInMillis = 1667251200000L;  // February 29, 2022
        int color = Color.RED;

        setCalendarCellColorForDate(calendarView, dateInMillis, color);
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
}
