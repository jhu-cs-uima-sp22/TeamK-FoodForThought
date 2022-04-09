package com.example.foodforthoughtapp.model.pantry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;

// Object model for a pantry
public class PantryInfo {
    public String name;
    public PantryLocation location;
    public String phone;
    public String website;
    public Map<String, PantryHours> hours;
    public ArrayList<Resource> resources;

    public PantryInfo() {}

    public PantryInfo(String name, PantryLocation location, String phone, String website,
                      Map<String, PantryHours> hours, String t, int c) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.website = website;
        this.hours = hours;
        Resource res = new Resource(t, c);
        this.resources.add(res);
    }

    @Override
    public int hashCode() {
        return (name + " " + " " + " " + phone + " " + website).hashCode();
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    @Override
    public boolean equals(Object other) {
        PantryInfo otherPantry = (PantryInfo) other;
        return this.name.equals(otherPantry.name) && this.phone.equals(otherPantry.phone) && this.website.equals(otherPantry.website);
    }
}
