package com.example.foodforthoughtapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.foodforthoughtapp.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFrag extends Fragment {

    private String userId;
    private String userEmail;
    private MainActivity myact;
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    private UserInfo user;
    // UI elements
    private TextInputEditText nameBox;
    private TextInputEditText dobBox;
    private TextInputEditText phoneBox;
    private TextInputEditText emailBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.frag_settings, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        dbref.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("SettingsFrag", task.getException().toString());
                }
                user = task.getResult().getValue(UserInfo.class);
                initUI();
            }
        });

        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("Settings");

        nameBox =  view.findViewById(R.id.nameInput);
        dobBox = view.findViewById(R.id.dobInput);
        phoneBox = view.findViewById(R.id.phoneInput);
        emailBox = view.findViewById(R.id.emailInput);

        return view;
    }

    private void initUI() {
        nameBox.setText(user.fname + " " + user.lname);
        dobBox.setText(user.DOB);
        phoneBox.setText(user.phone);
        emailBox.setText(userEmail);

        nameBox.setEnabled(false);
        dobBox.setEnabled(false);

        emailBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newEmail = emailBox.getText().toString();
                //emailBox.setText(newEmail);
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(newEmail);
            }
        });

        phoneBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newPhone = phoneBox.getText().toString();
                dbref.child("users").child(userId).child("phone").setValue(newPhone);
                //phoneBox.setText(newPhone);
            }
        });
    }

}
