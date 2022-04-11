package com.example.foodforthoughtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodforthoughtapp.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText birthDate;
    private FirebaseAuth auth;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.nav_center);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.app_sign_up);

        final Button signUp = findViewById(R.id.signup_confirm);
        final Button login = findViewById(R.id.signup_return_login);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);

        birthDate = findViewById(R.id.signup_DOB);
        birthDate.addTextChangedListener(mDateEntryWatcher);

        EditText phoneNumber = findViewById(R.id.signup_phone_number);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_return_login:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                this.finish();
                break;
            case R.id.signup_confirm:
                // TODO: Add input verification / restrictions for password confirmation checking
                // TODO: Change to launch map activity without this activity being tied?
                EditText signupName = findViewById(R.id.signup_name);
                EditText signupDOB = findViewById(R.id.signup_DOB);
                EditText signupPhoneNum = findViewById(R.id.signup_phone_number);
                EditText signupEmail = findViewById(R.id.signup_email);
                EditText signupPassword = findViewById(R.id.signup_password);
                EditText signupPasswordConfirm = findViewById(R.id.signup_password_confirm);
                //signUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)

                // create account and authenticate user
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String passwordConfirm = signupPasswordConfirm.getText().toString();

                if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    Log.w("AUTH", "signUp:failure, empty email/password");
                    Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.auth_invalid_fields), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }

                if (!password.equals(passwordConfirm)) {
                    Log.w("AUTH", "signUp:failure, passwords do not match");
                    Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.auth_password_mismatch), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // user account created successfully
                        Log.d("AUTH", "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        // add this user's details to the UserInfo database
                        String[] name = signupName.getText().toString().split(" ");
                        String DOB = signupDOB.getText().toString();
                        String phone = signupPhoneNum.getText().toString();
                        UserInfo userInfo = new UserInfo(name[0], name[1], phone, DOB);
                        db.child("users").child(user.getUid()).setValue(userInfo);
                        Intent intent = new Intent(this, MapsActivity.class);
                        startActivity(intent);
                        this.finish();
                        // TODO: TODO: direct to the launch map activity
                    } else {
                        // account not created successfully
                        Exception e = task.getException();
                        Log.w("AUTH", "createUserWithEmail:failure", e);
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.auth_email_collision), Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (e instanceof FirebaseAuthWeakPasswordException) {
                            Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.auth_weak_password), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private final TextWatcher mDateEntryWatcher = new TextWatcher() {

        private String current = "";
        private final String mmddyyyy = "MMDDYYYY";
        private final Calendar cal = Calendar.getInstance();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + mmddyyyy.substring(clean.length());
                } else {
                    int mon  = Integer.parseInt(clean.substring(0,2));
                    int day  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    mon = mon < 1 ? 1 : Math.min(mon, 12);
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year < 1900) ? 1900: Math.min(year, 2100);
                    cal.set(Calendar.YEAR, year);

                    day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                    clean = String.format("%02d%02d%02d", mon, day, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = Math.max(sel, 0);
                current = clean;
                birthDate.setText(current);
                birthDate.setSelection(Math.min(sel, current.length()));

                if(current.contains("Y") && birthDate.getError() == null)
                    birthDate.setError("Enter a valid date: MM/DD/YYYY");
                else if(!current.contains("Y"))
                    birthDate.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
}