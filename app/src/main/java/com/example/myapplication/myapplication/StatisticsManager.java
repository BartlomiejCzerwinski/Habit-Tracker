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

        setHabitsSpinner();
        setSelectDateListener();
        setProgressBarsNames();
    }

    public void setSelectDateListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendarToCheckSelectedDate.set(year, month, dayOfMonth);
                System.out.println("a");
                String habitName = getSelectedItemFromSpinner();
                if (habitName != "--Select Habit--") {
                    setTotalDoneProgressBar(habitName);
                }
            }
        });
    }

    public String getSelectedDateFromCalendar() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(calendarToCheckSelectedDate.getTime());
    }

    public String getDateXdaysBeforeSelectedDate(String selectedDate, int numberOfDays) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        Date date = dateFormat.parse(selectedDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -numberOfDays);

        return dateFormat.format(calendar.getTime());
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

    public String getSelectedItemFromSpinner() {
        Spinner spinner = (Spinner) binding.getRoot().findViewById(R.id.habitsSpinner);
        return spinner.getSelectedItem().toString();
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
                System.out.println("SELECTED DATE:" + getSelectedDateFromCalendar());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setProgressBarsNames() {
        TextView textViewLast7Days = (TextView) binding.getRoot().findViewById(R.id.textView_last7Days);
        TextView textViewLast30Days = (TextView) binding.getRoot().findViewById(R.id.textView_last30Days);
        TextView textViewTotal = (TextView) binding.getRoot().findViewById(R.id.textView_total);
        textViewLast7Days.setText("Last 7 days");
        textViewLast30Days.setText("Last 30 days");
        textViewTotal.setText("Total");

    }

    public void setTotalDoneText(DataBaseHelper dataBaseHelper, String habitName, long diffInDays) {
        TextView textView = (TextView) binding.getRoot().findViewById(R.id.text_total);
        textView.setText(dataBaseHelper.countDoneDays(habitName) + "/" + diffInDays);
    }

    public void setMonthDoneProgressBar(String habitName) throws ParseException {
        String startDate = getSelectedDateFromCalendar();
        String endDate = getDateXdaysBeforeSelectedDate(startDate, 29);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        ProgressBar progressBar = (ProgressBar) binding.getRoot().findViewById(R.id.progressBar2);

        int doneInLast30Days = dataBaseHelper.countDoneDaysFromTimePeriod(habitName, startDate, endDate);
        int progressValue = (int)((doneInLast30Days/(float)30)*100);
        progressBar.setProgress(progressValue);
        setMonthDoneText(doneInLast30Days, 30);
        setWeekDoneProgress(habitName);
    }

    public void setWeekDoneProgress(String habitName) throws ParseException {
        String startDate = getSelectedDateFromCalendar();
        String endDate = getDateXdaysBeforeSelectedDate(startDate, 6);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        ProgressBar progressBar = (ProgressBar) binding.getRoot().findViewById(R.id.progressBar);

        int doneInLast7Days = dataBaseHelper.countDoneDaysFromTimePeriod(habitName, startDate, endDate);
        int progressValue = (int)((doneInLast7Days/(float)7)*100);
        progressBar.setProgress(progressValue);
        setWeekDoneText(doneInLast7Days, 7);
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

}
