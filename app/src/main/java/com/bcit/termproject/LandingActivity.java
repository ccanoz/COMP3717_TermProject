package com.bcit.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LandingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
    }

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