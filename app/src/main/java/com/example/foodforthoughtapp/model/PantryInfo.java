package com.example.foodforthoughtapp.model;

import java.util.List;

// Object model for a pantry
public class PantryInfo {

    public String name;
    public String location;
    public String phone;
    public String website;
    public List<PantryHours> hours;

    public PantryInfo(String name, String location, String phone, String website, List<PantryHours> hours) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.website = website;
        this.hours = hours;
    }
}
