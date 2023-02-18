package com.example.myapplication.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class HomeManager {
    private ListView habitsListView;
    private Context context;
    LayoutInflater layoutInflater;

    public HomeManager(ListView habitsListView, Context context, LayoutInflater layoutInflater) {
        this.habitsListView = habitsListView;
        this.context = context;
        this.layoutInflater = layoutInflater;

        loadHabitsList();
    }

    public void loadHabitsList() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<String> habits = dataBaseHelper.getHabitsNamesListFromHabitsNamesTable();
        List<HabitModel> habitList = new ArrayList<>();

        for (String habitName : habits) {
            HabitModel habit = new HabitModel(habitName);
            System.out.println(habit.toString());
            habitList.add(habit);
        }

        ArrayAdapter<HabitModel> habitsArrayAdapter = new ArrayAdapter<HabitModel>(context, R.layout.habit_layout, habitList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) layoutInflater;
                    view = inflater.inflate(R.layout.habit_layout, parent, false);
                }

                CheckBox checkBox = view.findViewById(R.id.habit_item_checkbox);
                checkBox.setChecked(habitList.get(position).isSelected());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        habitList.get(position).setSelected(isChecked);
                        // update the database with the selected state of the habit
                    }
                });

                TextView textView = view.findViewById(R.id.habit_item_name);
                textView.setText(habitList.get(position).getName());

                return view;
            }
        };

            habitsListView.setAdapter(habitsArrayAdapter);
    }

}
