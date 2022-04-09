package com.example.foodforthoughtapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // check whether the user is already logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // the user is already authenticated
            Log.d("AUTH", "User " + user.getUid() + " is already authenticated");
            // TODO: launch the map activity
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            this.finish();
        }

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);

        final Button signUp = findViewById(R.id.welcome_sign_up);
        final Button login = findViewById(R.id.welcome_login);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
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