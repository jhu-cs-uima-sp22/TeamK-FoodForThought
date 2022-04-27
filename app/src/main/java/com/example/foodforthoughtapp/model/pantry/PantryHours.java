package com.example.foodforthoughtapp.model.pantry;

public class PantryHours {

    public String startTime;
    public String endTime;

    public PantryHours() {
    }

    public PantryHours(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isValid() {
        return startTime.compareTo(endTime) < 0;
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}