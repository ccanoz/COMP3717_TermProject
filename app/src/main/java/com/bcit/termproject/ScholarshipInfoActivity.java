package com.bcit.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ScholarshipInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_info);
        getSupportActionBar().hide();

    }
}