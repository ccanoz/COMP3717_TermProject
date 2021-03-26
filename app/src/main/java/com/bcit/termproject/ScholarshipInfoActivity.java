package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScholarshipInfoActivity extends AppCompatActivity {

    HashMap<String, String>  requirementsDesc = new HashMap<String, String>();
    List<String> requirements = new ArrayList<String>();
    RecyclerView rvRequirements;
    DatabaseReference dbScholarships;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_info);

        rvRequirements = findViewById(R.id.rv_requirements);

        // Nt: This should access a specific Scholarship by its id
        dbScholarships = FirebaseDatabase.getInstance().getReference("scholarship").child("-MVEqScsp1eOCjqVdpvs").child("Requirements");
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbScholarships.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requirementsDesc.clear();
                requirements.clear();
                for(DataSnapshot requirement: snapshot.getChildren()) {
                    String key = requirement.getKey();
                    requirementsDesc.put(key, requirement.getValue(String.class));
                    requirements.add(key);
                }

                RequirementsAdapter adapter = new RequirementsAdapter(requirementsDesc, requirements);
                rvRequirements.setAdapter(adapter);
                rvRequirements.setLayoutManager(new LinearLayoutManager(ScholarshipInfoActivity.this));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}