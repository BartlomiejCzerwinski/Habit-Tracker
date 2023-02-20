package com.example.myapplication.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class HomeManager {
    private ListView habitsListView;
    private Context context;
    private LayoutInflater layoutInflater;
    private View rootView;


    // popup window
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText habitName;
    private TextView popupList;
    private Button addHabit, cencel;


    public HomeManager(ListView habitsListView, Context context, LayoutInflater layoutInflater, TextView popupList, View rootView) {
        this.habitsListView = habitsListView;
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.popupList = popupList;
        this.rootView = rootView;

        loadHabitsList();
        createNewHabbit();
    }

    public void createNewHabbit() {
        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.add_habit_button);
        System.out.println("My button: "+ floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(context);
                final View newHabitPopupView = layoutInflater.inflate(R.layout.popup, null);
                habitName = (EditText) newHabitPopupView.findViewById(R.id.newhabitpopup_habit_name);

                addHabit = (Button) newHabitPopupView.findViewById(R.id.add_habit_button);// a.d. 1
                cencel = (Button) newHabitPopupView.findViewById(R.id.cencel_button);     // no coś takiego to by było
                dialogBuilder.setView(newHabitPopupView);
                dialog = dialogBuilder.create();
                dialog.show();
                addHabit.setText("Add habit");
                addHabit.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public  void onClick(View v){
                        String newHabitName = null;
                        try{
                            newHabitName = habitName.getText().toString();
                        }
                        catch (Exception e){

                        }
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        boolean success = dataBaseHelper.addHabitToHabitsNamesTable(newHabitName);
                        dialog.cancel();
                        //loadHabitsList();
                    }
                });

                cencel.setText("Cencel");
                cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        dialog.cancel();
                        //loadHabitsList();
                    }
                });
            }
        });
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
