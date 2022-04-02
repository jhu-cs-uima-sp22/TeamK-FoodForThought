package com.example.foodforthoughtapp;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAuth {

    private static String AUTH_TAG = "AUTH";
    private FirebaseAuth auth;

    public UserAuth() {
        // initialize this Firebase instance object in the onCreate for the main activity
        this.auth = FirebaseAuth.getInstance();
    }

    // example of creating a user's account -- can inline this code in the create account activity
    public void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // code to run upon successful creation of a user's account
                // TODO: launch activity that appears after the user logs in
                if (task.isSuccessful()) {
                    Log.d(AUTH_TAG, "createUserWithEmail:success");
                    FirebaseUser user = auth.getCurrentUser();
                    // TODO: update UI and/or launch the next activity
                } else {
                    // account creation failed
                    Log.w(AUTH_TAG, "createUserWithEmail:failure", task.getException());
                    // TODO: update UI accordingly with error toast -- replace null with actual context
                    Toast.makeText(null, "Invalid email address and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(AUTH_TAG, "signInWithEmail:success");
                    // store this user object in the application for future reference
                    FirebaseUser user = auth.getCurrentUser();
                    Log.d(AUTH_TAG, "Successfully logged in user " + user.getUid());
                } else {
                    // sign in failed
                    Log.w(AUTH_TAG, "signInWithEmail:failure", task.getException());
                }
            }
        });
    }

    public void signOut() {
        // by default this is only called when the user is logged in
        auth.signOut();
        Log.d(AUTH_TAG, "signOut:success");
    }
}
