package com.example.foodforthoughtapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsFrag extends Fragment {

    private String userId;
    private MainActivity myact;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("Entered settings onCreateView");

        View view =inflater.inflate(R.layout.frag_settings, container, false);

        System.out.println("Inflated settings view in fragment");

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("Settings");

        TextInputEditText nameBoxEditText = (TextInputEditText) view.findViewById(R.id.nameBoxInput);
        nameBoxEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Set name to the changed text
            }
        });

        TextInputEditText emailBoxEditText = (TextInputEditText) view.findViewById(R.id.emailBoxInput);
        emailBoxEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Set email to the changed text
            }
        });

        TextInputEditText phoneBoxEditText = (TextInputEditText) view.findViewById(R.id.phoneBoxInput);
        phoneBoxEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Set phone to the changed text
            }
        });


        return view;
    }

}
