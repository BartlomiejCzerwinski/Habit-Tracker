package com.example.myapplication.myapplication;
import java.util.Date;
import java.text.SimpleDateFormat;
public class HabitModel {
    private String name;
    private int id;
    private boolean isDone;
    public HabitModel(String name) {
        int intdate = getIdFromDate();
        this.name = name;
        this.id = intdate;
        this.isDone = false;
    }

    public HabitModel(String name, int id, boolean isDone) {
        this.name = name;
        this.id = id;
        this.isDone = isDone;
    }

    public int getIdFromDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        return Integer.parseInt(formatter.format(date));
    }

    @Override
    public String toString() {
        return "HabitModel{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", isDone=" + isDone +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isSelected() {
        if (isDone)
            return true;
        else
            return false;
    }

    public void setSelected(boolean isChecked) {
        isDone=true;
    }
}
