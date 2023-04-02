package com.example.myapplication.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Binder;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.TextView;


import com.example.myapplication.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.myapplication.databinding.FragmentSettingsBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {

    private String USER_NAME_FILE = "config_username.txt";
    private String COLOUR_FILE = "config_selected_colour.txt";

    private FragmentSettingsBinding binding;
    private ActivityMainBinding mainBinding;
    private TextView textViewForUsername;
    private Spinner spinner;
    private Context context;

    public SettingsManager(FragmentSettingsBinding binding, TextView textView,  Context context) {
        this.binding = binding;
        this.textViewForUsername = textView;
        this.context = context;
        this.spinner = (Spinner) binding.getRoot().findViewById(R.id.colourSpinner);
        setActualUserNameInEditTextField();
        getNewUserName();
    }

    public SettingsManager(ActivityMainBinding binding) {
        this.mainBinding = binding;
    }

    public void setActualUserNameInEditTextField() {
        EditText editText = binding.getRoot().findViewById(R.id.editTextTextPersonName);
        editText.setText(getUserNameFromFile());
    }

    public void setActualUserNameInNavigation(String name) {
        textViewForUsername.setText("Hello, " + name);
    }

    public void getNewUserName() {

        binding.getRoot().findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.getRoot().findViewById(R.id.editTextTextPersonName) != null) {
                    TextView textSettingsView = (TextView) binding.getRoot().findViewById(R.id.editTextTextPersonName);
                    String textSettingsValue = textSettingsView.getText().toString();

                    try {
                        File file = new File(binding.getRoot().getContext().getFilesDir(), USER_NAME_FILE);
                        file.setReadable(true, true);
                        file.setWritable(true, true);
                        file.setExecutable(true, true);

                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(textSettingsValue.getBytes(StandardCharsets.UTF_8));
                        fos.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setActualUserNameInNavigation(textSettingsValue);
                }
            }
        });
    }

    public String getUserNameFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        File file = null;
        try {
            if(mainBinding != null) {
                file = new File(mainBinding.getRoot().getContext().getFilesDir(), USER_NAME_FILE);
            }
            else
            {
                file = new File(binding.getRoot().getContext().getFilesDir(), USER_NAME_FILE);
            }
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            reader.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }



}
