package com.example.foodforthoughtapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    private UserInfo user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.frag_settings, container, false);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        dbref.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                user = task.getResult().getValue(UserInfo.class);
            }
        });

        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("Settings");

        TextView nameBox = view.findViewById(R.id.nameBox);
        TextView dobBox = view.findViewById(R.id.dobBox);

        nameBox.setText(user.getFname() + " " + user.getLname());
        dobBox.setText(user.getDOB());

        EditText phoneBox = view.findViewById(R.id.editPhone);
        phoneBox.setText(user.getPhone());

        EditText emailBox = view.findViewById(R.id.editEmail);
        emailBox.setText(userEmail);

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
                phoneBox.setText(newPhone);
            }
        });

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
                emailBox.setText(newEmail);
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(newEmail);
            }
        });





        return view;
    }

}
