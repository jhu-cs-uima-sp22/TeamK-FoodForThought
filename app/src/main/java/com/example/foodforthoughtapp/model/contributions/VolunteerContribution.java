package com.example.foodforthoughtapp.model.contributions;

import com.example.foodforthoughtapp.model.pantry.PantryHours;

public class VolunteerContribution {
    public String pantryID;
    public PantryHours hours;

    public VolunteerContribution(String pantryID, PantryHours hours) {
        this.pantryID = pantryID;
        this.hours = hours;
    }
}
