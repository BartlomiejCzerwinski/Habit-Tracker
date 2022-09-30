package com.example.myapplication.myapplication;
import java.util.Date;
import java.text.SimpleDateFormat;
public class HabitModel {
    private String name;
    private int id;
    private boolean isDone;

    public HabitModel(String name) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        int intdate = Integer.parseInt(formatter.format(date));
        this.name = name;
        this.id = intdate;
        this.isDone = false;
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
}
