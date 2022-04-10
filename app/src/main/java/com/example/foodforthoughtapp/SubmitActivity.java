package com.example.foodforthoughtapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SubmitActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_submit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.welcome_login:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;
            case R.id.welcome_sign_up:
                Intent signUp = new Intent(this, SignUpActivity.class);
                startActivity(signUp);
                break;
            default: break;
        }
    }
}