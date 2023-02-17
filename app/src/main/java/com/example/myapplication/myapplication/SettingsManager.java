package com.example.myapplication.myapplication;

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
import android.widget.TextView;


import com.example.myapplication.myapplication.databinding.FragmentSettingsBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SettingsManager {

    private String USER_NAME_FILE = "config_username.txt";
    FragmentSettingsBinding binding;

    public SettingsManager(FragmentSettingsBinding binding) {
        this.binding = binding;
        System.out.println(readFromFile());
    }

    public void fct() {
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
                        System.out.println("File saved successfully!");

                        System.out.println(readFromFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String readFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(binding.getRoot().getContext().getFilesDir(), USER_NAME_FILE);
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
