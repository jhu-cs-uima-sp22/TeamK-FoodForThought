package com.example.foodforthoughtapp.model.pantry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

// Object model for a pantry
public class PantryInfo implements Parcelable {
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

    protected PantryInfo(Parcel in) {
        name = in.readString();
        phone = in.readString();
        website = in.readString();
    }

    public static final Creator<PantryInfo> CREATOR = new Creator<PantryInfo>() {
        @Override
        public PantryInfo createFromParcel(Parcel in) {
            return new PantryInfo(in);
        }

        @Override
        public PantryInfo[] newArray(int size) {
            return new PantryInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(website);
    }
}
