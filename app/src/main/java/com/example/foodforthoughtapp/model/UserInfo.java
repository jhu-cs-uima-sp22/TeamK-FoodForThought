package com.example.foodforthoughtapp.model;

// Object model for a user
public class UserInfo {

    public String fname;
    public String lname;
    public String phone;
    public String DOB;

    public UserInfo() {}

    public UserInfo(String fname, String lname, String phone, String DOB) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.DOB = DOB;
    }

//    public String getFname() {
//        return fname;
//    }
//
//    public String getLname() {
//        return lname;
//    }
//
//    public String getPhone(){return phone;}
//
//    public String getDOB() {
//        return DOB;
//    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", phone='" + phone + '\'' +
                ", DOB='" + DOB + '\'' +
                '}';
    }
}