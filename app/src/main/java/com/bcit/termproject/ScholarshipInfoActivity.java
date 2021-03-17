package com.bcit.termproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScholarshipInfoActivity extends AppCompatActivity {

    HashMap<String, String>  requirementsDesc = new HashMap<String, String>();
    List<String> requirements = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_info);

        RecyclerView rvRequirements = findViewById(R.id.rv_requirements);

        requirements.add("Employment Status");
        requirements.add("Academic Transcript");

        Log.d("req", requirements.get(0));
        requirementsDesc.put("Employment Status", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.");
        requirementsDesc.put("Academic Transcript", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.");
        Log.d("desc", requirementsDesc.get(requirements.get(0)));

        RequirementsAdapter adapter = new RequirementsAdapter(requirementsDesc, requirements);
        rvRequirements.setAdapter(adapter);
        rvRequirements.setLayoutManager(new LinearLayoutManager(this));
    }
}