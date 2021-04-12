package com.bcit.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailET;
    private EditText passwordET;

    /**
     * Initializes the email and password views, as well as the instance of FirebaseAuth in order
     * to perform the authentication of the User.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email_ET_login);
        passwordET = findViewById(R.id.password_ET_login);

    }

    /**
     * Click listener method for the login button.
     * Retrieves the email and password from the input fields and processes the login.
     * @param v a View, the login button
     */
    public void login(View v){
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}