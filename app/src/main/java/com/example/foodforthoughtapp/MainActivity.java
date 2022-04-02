package com.example.foodforthoughtapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private UserAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = new UserAuth();
        test();
    }

    private void test() {
        auth.createAccount("haydenn@jhu.edu", "test123");
    }
}