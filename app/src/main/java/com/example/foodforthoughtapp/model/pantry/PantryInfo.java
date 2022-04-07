package com.example.foodforthoughtapp.model.pantry;

import java.util.Map;

// Object model for a pantry
public class PantryInfo {
    public String name;
    public PantryLocation location;
    public String phone;
    public String website;
    public Map<String, PantryHours> hours;
    public Map<String, Integer> resources;

    public PantryInfo() {}

    public PantryInfo(String name, PantryLocation location, String phone, String website,
                      Map<String, PantryHours> hours, Map<String, Integer> resources) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.website = website;
        this.hours = hours;
        this.resources = resources;
    }
}
