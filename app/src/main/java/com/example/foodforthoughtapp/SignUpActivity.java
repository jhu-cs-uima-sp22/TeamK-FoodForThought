package com.example.foodforthoughtapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText birthDate;
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

        birthDate = findViewById(R.id.DOB);
        birthDate.addTextChangedListener(mDateEntryWatcher);

        EditText phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
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
                // TODO: Add input verification
                // TODO: Change to launch map activity without this activity being tied?
                Intent signUp = new Intent(this, SignUpActivity.class);
                //signUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signUp);
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

                birthDate.setError("Enter a valid date: MM/DD/YYYY");

            }
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    };
}