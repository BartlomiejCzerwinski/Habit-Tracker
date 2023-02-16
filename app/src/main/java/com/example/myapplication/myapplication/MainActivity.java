package com.example.myapplication.myapplication;

import android.os.Bundle;
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

import com.example.myapplication.myapplication.ui.home.HomeFragment;
import com.example.myapplication.myapplication.ui.home.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.myapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    // popup window
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText habitName;
    private TextView popupList;
    private Button addHabit, cencel;
    private ListView habitsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        dataBaseHelper.createHabitsTables();
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewHabbit();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings,
                R.id.nav_statistics, R.id.nav_tutorial)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        loadHabitsList();

    }

    public void loadHabitsList() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        List<String> habits = dataBaseHelper.getHabitsNamesListFromHabitsNamesTable();
        List<HabitModel> habitList = new ArrayList<>();

        for (String habitName : habits) {
            HabitModel habit = new HabitModel(habitName);
            System.out.println(habit.toString());
            habitList.add(habit);
        }

        ArrayAdapter<HabitModel> habitsArrayAdapter = new ArrayAdapter<HabitModel>(MainActivity.this, R.layout.habit_layout, habitList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
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

        habitsListView = findViewById(R.id.habits_list_view);
        habitsListView.setAdapter(habitsArrayAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }



    public void createNewHabbit(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View newHabitPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        habitName = (EditText) newHabitPopupView.findViewById(R.id.newhabitpopup_habit_name);

        addHabit = (Button) newHabitPopupView.findViewById(R.id.add_habit_button);// a.d. 1
        cencel = (Button) newHabitPopupView.findViewById(R.id.cencel_button);     // no coś takiego to by było
        dialogBuilder.setView(newHabitPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        popupList = findViewById(R.id.popuplist);
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
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean success = dataBaseHelper.addHabitToHabitsNamesTable(newHabitName);
                dialog.cancel();
                loadHabitsList();
            }
        });

        cencel.setText("Cencel");
        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                dialog.cancel();
                loadHabitsList();
            }
        });

    }

}