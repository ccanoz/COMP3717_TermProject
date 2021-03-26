package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScholarshipInfoActivity extends AppCompatActivity {

    HashMap<String, String>  requirementsDesc = new HashMap<String, String>();
    List<String> requirements = new ArrayList<String>();
    RecyclerView rvRequirements;
    TextView scholName;
    TextView scholAbout;
    TextView scholAmount;
    TextView scholOrg;
    DatabaseReference dbScholarships;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_info);

        scholName = findViewById(R.id.textView_scholName);
        scholAbout = findViewById(R.id.textView_scholAboutTxt);
        scholAmount = findViewById(R.id.textView_schol_amount);
        scholOrg = findViewById(R.id.textView_schol_orgName);

        rvRequirements = findViewById(R.id.rv_requirements);

        // Nt: This should access a specific Scholarship by its id
        dbScholarships = FirebaseDatabase.getInstance().getReference("scholarship").child("-MVEqScsp1eOCjqVdpvs");
    }


    /**
     * Populate the activity layout with the scholarship details retrieved from
     * the database.
     */
    @Override
    protected void onStart() {
        super.onStart();
        dbScholarships.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requirementsDesc.clear();
                requirements.clear();

                setScholarshipInfo(snapshot);
                setScholarshipRequirements(snapshot);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Retrieves a scholarship's name, description, amount, and organization name from a
     * DataSnapshot. Sets corresponding text fields in the layout to the values retrieved.
     *
     * @param snapshot of a Scholarship
     */
    private void setScholarshipInfo(@NonNull  DataSnapshot snapshot) {
        String name = snapshot.child("name").getValue(String.class);
        String description = snapshot.child("about").getValue(String.class);
        Long amount = snapshot.child("amount").getValue(Long.class);
        String organization  =snapshot.child("organization").getValue(String.class);

        // Format with commas
        String amountString = "$ " + NumberFormat.getInstance().format(amount);

        scholName.setText(name);
        scholAbout.setText(description);
        scholAmount.setText(amountString);
        scholOrg.setText(organization);
    }

    /**
     * Retrieves the requirements for a scholarship from a DataSnapshot and
     * populates the recyclerView with the requirement title and description.
     *
     * @param snapshot of a Scholarship
     */
    private void setScholarshipRequirements(@NonNull DataSnapshot snapshot) {
        DataSnapshot requirementsSnapshot = snapshot.child("Requirements");

        // Get each requirement and store in an array (titles) and HashMap (titles and description)
        for(DataSnapshot requirement: requirementsSnapshot.getChildren()) {
            String key = requirement.getKey();
            requirementsDesc.put(key, requirement.getValue(String.class));
            requirements.add(key);
        }

        RequirementsAdapter adapter = new RequirementsAdapter(requirementsDesc, requirements);
        rvRequirements.setAdapter(adapter);
        rvRequirements.setLayoutManager(new LinearLayoutManager(ScholarshipInfoActivity.this));
    }
}