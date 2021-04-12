package com.bcit.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LandingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    /**
     * Calls the parent's onCreate and initializes the FirebaseAuth instance mAuth to check if the
     * user is logged in or not.
     * @param savedInstanceState a Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Calls the parent's method onStart and checks if there is a Use that is logged in.
     * If there is, this method redirects the authenticated user to the MainActivity.
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    /**
     * Click listener for the login button in the landing page.
     * @param v a View, the login Button
     */
    public void goToLogin(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * Click listener for the sign up in the landing page.
     * @param v a View, the signup Button
     */
    public void goToSignup(View v){
        startActivity(new Intent(this, SignUpActivity.class));
    }
}