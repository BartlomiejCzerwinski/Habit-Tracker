package com.example.myapplication.myapplication;

import android.content.Context;
import android.content.DialogInterface;
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
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(context);
                final View newHabitPopupView = layoutInflater.inflate(R.layout.popup, null);
                habitName = (EditText) newHabitPopupView.findViewById(R.id.newhabitpopup_habit_name);

                addHabit = (Button) newHabitPopupView.findViewById(R.id.add_habit_button);
                cencel = (Button) newHabitPopupView.findViewById(R.id.cencel_button);
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
                        dataBaseHelper.addHabitToHabitsNamesTable(newHabitName);
                        dataBaseHelper.createHabitsTables();
                        dialog.cancel();
                        loadHabitsList();
                    }
                });

                cencel.setText("Cencel");
                cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        dialog.cancel();
                        loadHabitsList();
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
            HabitModel habit = new HabitModel(habitName, dataBaseHelper.getIdFromDate(), dataBaseHelper.getHabitDailyStatus(habitName));
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
                        String habitName = habitList.get(position).getName();
                        if (isChecked) {
                            dataBaseHelper.addHabitDailyStatus(habitName, true);
                        }
                        else {
                            dataBaseHelper.addHabitDailyStatus(habitName, false);
                        }
                    }
                });

                TextView textView = view.findViewById(R.id.habit_item_name);
                textView.setText(habitList.get(position).getName());

                deleteHabit(view, habitList, position);
                return view;
            }

        };

            habitsListView.setAdapter(habitsArrayAdapter);
    }

    public void deleteHabit(View view, List<HabitModel> habitList, int position) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete habit");
                builder.setMessage("Are you sure you want to delete this habit?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String habitName = habitList.get(position).getName();
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        dataBaseHelper.deleteHabitFromDb(habitName);
                        System.out.println(habitName);
                        habitList.remove(position);
                        loadHabitsList();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return true;
            }
        });
    }

}
