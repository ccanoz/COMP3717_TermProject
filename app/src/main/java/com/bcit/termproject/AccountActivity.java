package com.bcit.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    FirebaseUser currAuthUser;
    User currUser;

    RecyclerView rvAccDetails;

    private HashMap<String, String> accDetailMap = new HashMap<>();
    private List<String> accDetailList = new ArrayList<>();

    DatabaseReference dbUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().hide();

        rvAccDetails = findViewById(R.id.rv_account_details);

        currAuthUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("current user", currAuthUser.getUid());

        dbUserInfo = FirebaseDatabase.getInstance().getReference("user");
    }

    public void updateUser(String field, String updateValue) {

        Log.d("Updating: ", field + " to " + updateValue);

        Task<Void> setValueTask = dbUserInfo.child(field).setValue(updateValue);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(AccountActivity.this, "Updated " + field, Toast.LENGTH_LONG).show();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AccountActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Populate the activity layout with the user details retrieved from
     * the database.
     */
    @Override
    protected void onStart() {
        super.onStart();
        dbUserInfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUser = snapshot.child(currAuthUser.getUid()).getValue(User.class);
                setAccountDetails();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     *
     *
     */
    private void setAccountDetails() {
        accDetailList.clear();
        accDetailMap.clear();


        accDetailList.add("name");
        accDetailList.add("DOB");
        accDetailList.add("gender");
        accDetailList.add("income");
        accDetailList.add("nationality");
        accDetailList.add("employment status");

        accDetailMap.put("name", currUser.getName());
        accDetailMap.put("DOB", currUser.getDOB());
        accDetailMap.put("gender", currUser.getGender());
        accDetailMap.put("income", currUser.getYearlyIncome());
        accDetailMap.put("nationality", currUser.getNationality());

        String employStatus = currUser.getEmployed()? "Employed" : "Unemployed";
        accDetailMap.put("employment status", employStatus);

        AccountAdapter adapter = new AccountAdapter(accDetailMap, accDetailList);
        rvAccDetails.setAdapter(adapter);
        rvAccDetails.setLayoutManager(new LinearLayoutManager(AccountActivity.this));
    }

}
