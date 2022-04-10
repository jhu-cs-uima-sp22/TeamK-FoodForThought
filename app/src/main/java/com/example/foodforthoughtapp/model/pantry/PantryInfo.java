package com.example.foodforthoughtapp.model.pantry;

import java.util.List;
import java.util.Map;

// Object model for a pantry
public class PantryInfo {
    public String name;
    public PantryLocation location;
    public String phone;
    public String website;
    public Map<String, PantryHours> hours;
    public List<Resource> resources;

    public PantryInfo() {}

    public PantryInfo(String name, PantryLocation location, String phone, String website,
                      Map<String, PantryHours> hours, List<Resource> resources) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.website = website;
        this.hours = hours;
        // Resource res = new Resource(t, c);
        // this.resources.add(res);
        this.resources = resources;
    }

    @Override
    public int hashCode() {
        return (name + " " + " " + " " + phone + " " + website).hashCode();
    }

    public List<Resource> getResources() {
        return resources;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        PantryInfo otherPantry = (PantryInfo) other;
        return this.name.equals(otherPantry.name) && this.phone.equals(otherPantry.phone) && this.website.equals(otherPantry.website);
    }
}
