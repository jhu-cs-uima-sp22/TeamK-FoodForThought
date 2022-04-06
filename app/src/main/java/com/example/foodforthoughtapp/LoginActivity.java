package com.example.foodforthoughtapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.nav_center);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.app_log_in);

        final Button signUp = findViewById(R.id.login_return_signup);
        final Button login = findViewById(R.id.login_confirm);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_confirm:
                // TODO: Add input verification
                // TODO: Change to launch map activity without this activity being tied
                EditText loginEmail = findViewById(R.id.login_email);
                EditText loginPassword = findViewById(R.id.login_password);
                Intent login = new Intent(this, LoginActivity.class);
                //login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
                break;
            case R.id.login_return_signup:
                Intent signUp = new Intent(this, SignUpActivity.class);
                startActivity(signUp);
                this.finish();
                break;
            default:
                break;
        }
    }
}