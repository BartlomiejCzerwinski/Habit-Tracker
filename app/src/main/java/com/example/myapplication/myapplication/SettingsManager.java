package com.example.myapplication.myapplication;

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


import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {

    private String USER_NAME_FILE = "username.txt";
    private Button button;

    public SettingsManager(Button button) {
        this.button = button;
    }

    public void fct() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("testttttttttowe");
            }
        });
    }
}
