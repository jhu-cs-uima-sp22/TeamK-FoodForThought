package com.example.foodforthoughtapp.model.pantry;

public class VolDateTime {
    public String date;
    public PantryHours time;

    public VolDateTime(String date, String start, String end) {
        this.date = date;
        this.time = new PantryHours(start, end);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String start, String end) {
        time.setStartTime(start);
        time.setEndTime(end);
    }
}
