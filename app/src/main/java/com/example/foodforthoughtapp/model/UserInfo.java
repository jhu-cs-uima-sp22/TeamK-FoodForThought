package com.example.foodforthoughtapp.model;

// Object model for a user
public class UserInfo {

    public String fname;
    public String lname;
    public String phone;
    public String DOB;

    public UserInfo(String fname, String lname, String phone, String DOB) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.DOB = DOB;
    }
}