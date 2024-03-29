package com.example.foodforthoughtapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

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
                // TODO: Change to launch map activity without this activity being tied
                EditText loginEmail = findViewById(R.id.login_email);
                EditText loginPassword = findViewById(R.id.login_password);
                //login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Log.w("AUTH", "signInWithEmail:failure, empty email/password");
                    Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.auth_invalid_credentials), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // successful login
                                Log.d("AUTH", "signInWithEmail:success");
                                // store this user object in the application for future reference
                                FirebaseUser user = auth.getCurrentUser();
                                Log.d("AUTH", "Successfully logged in user " + user.getUid());
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                                this.finish();
                            } else {
                                // unsuccessful login
                                Log.w("AUTH", "signInWithEmail:failure", task.getException());
                                Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.auth_invalid_credentials), Toast.LENGTH_SHORT);
                                toast.show();
                                loginPassword.setText("");
                            }
                        });
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