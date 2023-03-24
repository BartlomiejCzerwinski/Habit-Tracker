package com.example.myapplication.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.myapplication.databinding.FragmentSettingsBinding;
import com.example.myapplication.myapplication.databinding.FragmentStatisticsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticsManager {
    private CalendarView calendarView;
    private Context context;
    private FragmentStatisticsBinding binding;
    private Spinner spinner;
    private final Calendar calendarToCheckSelectedDate = Calendar.getInstance();

    public StatisticsManager(CalendarView calendarView, Context context, FragmentStatisticsBinding binding) {
        this.calendarView = calendarView;
        this.context = context;
        this.binding = binding;
        this.spinner = (Spinner) binding.getRoot().findViewById(R.id.habitsSpinner);
        this.calendarView = (CalendarView) binding.getRoot().findViewById(R.id.calendarView3);

        long dateInMillis = 1667251200000L;  // February 29, 2022
        int color = Color.RED;

        setCalendarCellColorForDate(calendarView, dateInMillis, color);
        setHabitsSpinner();
        setSelectDateListener();
    }

    public void setSelectDateListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendarToCheckSelectedDate.set(year, month, dayOfMonth);

            }
        });
    }

    public String getSelectedDateFromCalendar() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendarToCheckSelectedDate.getTime());
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
        habitsNamesList.add("--Select Habit--");
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
               if (spinner.getSelectedItem().toString() == "--Select Habit--") {

               }
               else {
                    String habitName = spinner.getSelectedItem().toString();
                    setTotalDoneProgressBar(habitName);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
    public DataBaseHelper getDataBaseHelper() {
        return new DataBaseHelper(context);
    }

    public void setTotalDoneProgressBar(String habitName) {
        DataBaseHelper dataBaseHelper = getDataBaseHelper();
        String sd = dataBaseHelper.getHabitStartDate(habitName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(sd);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                String localDate = LocalDate.now().toString();
                Date now = sdf.parse(localDate);
                long diffInDays = (now.getTime() - startDate.getTime())/ (24 * 60 * 60 * 1000);
                diffInDays += 1;
                ProgressBar progressBar = (ProgressBar) binding.getRoot().findViewById(R.id.progressBar3);
                int progressValue = (int)(((float)dataBaseHelper.countDoneDays(habitName)/(float)diffInDays)*100);
                progressBar.setProgress(progressValue);
                setTotalDoneText(dataBaseHelper, habitName, diffInDays);
                setMonthDoneProgressBar(habitName);
                System.out.println("first week: " +getFirstDayOfCurrentWeek());
                System.out.println("SELECTED DATE:" + getSelectedDateFromCalendar());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setTotalDoneText(DataBaseHelper dataBaseHelper, String habitName, long diffInDays) {
        TextView textView = (TextView) binding.getRoot().findViewById(R.id.text_total);
        textView.setText(dataBaseHelper.countDoneDays(habitName) + "/" + diffInDays);
    }

    public void setMonthDoneProgressBar(String habitName) throws ParseException {
        Date firstDayOfMonth = getFirstDayOfCurrentMonth();
        Date today = getTodayDate();
        long diffInDays = (today.getTime() - firstDayOfMonth.getTime())/ (24 * 60 * 60 * 1000);
        diffInDays += 1;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        ProgressBar progressBar = (ProgressBar) binding.getRoot().findViewById(R.id.progressBar2);
        int monthDoneDays = dataBaseHelper.countMonthDoneDays(habitName, firstDayOfMonth);
        int progressValue = (int)((monthDoneDays/(float)diffInDays)*100);
        progressBar.setProgress(progressValue);
        setMonthDoneText(monthDoneDays, diffInDays);
        setWeekDoneProgress(habitName);

    }

    public void setWeekDoneProgress(String habitName) throws ParseException {
        Date firstDayOfWeek = getFirstDayOfCurrentWeek();
        Date today = getTodayDate();
        long diffInDays = (today.getTime() - firstDayOfWeek.getTime())/ (24 * 60 * 60 * 1000);
        diffInDays += 1;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        ProgressBar progressBar = (ProgressBar) binding.getRoot().findViewById(R.id.progressBar);
        int weekDoneDays = dataBaseHelper.countWeekDoneDays(habitName, firstDayOfWeek);
        int progressValue = (int)(((float)weekDoneDays/(float)diffInDays)*100);
        System.out.println("PROGRESS VALUE"+ progressValue);
        progressBar.setProgress(progressValue);
        setWeekDoneText(weekDoneDays, diffInDays);
    }

    public void setMonthDoneText(int monthDoneDays, long diffInDays) {
        TextView textView = (TextView) binding.getRoot().findViewById(R.id.text_month);
        textView.setText(monthDoneDays + "/" + diffInDays);

    }

    public void setWeekDoneText(int weekDoneDays, long diffInDays) {
        TextView textView = (TextView) binding.getRoot().findViewById(R.id.text_week);
        textView.setText(weekDoneDays + "/" + diffInDays);

    }

    public Date getTodayDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = LocalDate.now().toString();
        }
        Date now = sdf.parse(localDate);
        return now;
    }

    public Date getFirstDayOfCurrentMonth() {
        LocalDate today = null;
        LocalDate firstDayOfMonth = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Date date = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public Date getFirstDayOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date firstDayOfWeek = calendar.getTime();
        return firstDayOfWeek;
    }

}
