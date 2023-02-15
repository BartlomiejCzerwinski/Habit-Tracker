package com.example.myapplication.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        List<String> everyone = dataBaseHelper.getEveryone();

        ArrayAdapter habitsArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, everyone);
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
                boolean success = dataBaseHelper.addOne(newHabitName);
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