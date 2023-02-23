package com.example.myapplication.myapplication.ui.statistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.myapplication.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatisticsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StatisticsViewModel() {
        mText = new MutableLiveData<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formatedDate = sdf.format(date);
        mText.setValue(formatedDate);
    }

    public LiveData<String> getText() {
        return mText;
    }
}