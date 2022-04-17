package com.example.foodforthoughtapp.model.contributions;

import com.example.foodforthoughtapp.model.pantry.PantryHours;

public class VolunteerContribution extends Contribution {
    public String date;
    public String pantryID;
    public PantryHours hours;

    public VolunteerContribution() {}

    public VolunteerContribution(String date, String pantryID, PantryHours hours) {
        this.date = date;
        this.pantryID = pantryID;
        this.hours = hours;
    }

    @Override
    public String toString() {
        return hours.toString() + " on " + date;
    }
}
